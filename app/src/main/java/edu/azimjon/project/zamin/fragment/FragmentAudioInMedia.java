package edu.azimjon.project.zamin.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.azimjon.project.zamin.R;
import edu.azimjon.project.zamin.adapter.AudioNewsAdapter;
import edu.azimjon.project.zamin.addition.Constants;
import edu.azimjon.project.zamin.addition.MySettings;
import edu.azimjon.project.zamin.application.MyApplication;
import edu.azimjon.project.zamin.databinding.FooterNoConnectionBinding;
import edu.azimjon.project.zamin.databinding.WindowAudioInsideMediaBinding;
import edu.azimjon.project.zamin.databinding.WindowNoConnectionBinding;
import edu.azimjon.project.zamin.events.NetworkStateChangedEvent;
import edu.azimjon.project.zamin.events.PlayerStateEvent;
import edu.azimjon.project.zamin.model.SimpleNewsModel;
import edu.azimjon.project.zamin.mvp.presenter.PresenterAudioInMedia;
import edu.azimjon.project.zamin.mvp.view.IFragmentAudioInMedia;
import edu.azimjon.project.zamin.util.MyUtil;

import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_CONNECTION;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_NO_ITEMS;
import static edu.azimjon.project.zamin.addition.Constants.MESSAGE_OK;
import static edu.azimjon.project.zamin.addition.Constants.NETWORK_STATE_CONNECTED;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_HIDE;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_NEXT;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_OPENED_GET_HEIGHT;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_PAUSE_ICON;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_PLAY;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_PLAY_ICON;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_PREV;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_PROGRESS_CHANGED;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_RESET;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_STOP;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_TITLE;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_UPDATE;
import static edu.azimjon.project.zamin.events.PlayerStateEvent.PLAYER_UPDATE_TIME;

public class FragmentAudioInMedia extends Fragment implements IFragmentAudioInMedia, MediaPlayer.OnErrorListener, SwipeRefreshLayout.OnRefreshListener, AudioNewsAdapter.IMyPlayer {


    //TODO: Constants here
    LinearLayoutManager manager;
    boolean isConnected_to_Net = true;
    MediaPlayer mediaPlayer;


    //TODO: variables here
    WindowAudioInsideMediaBinding binding;
    WindowNoConnectionBinding bindingNoConnection;
    FooterNoConnectionBinding bindingFooter;
    View viewHeader;
    View viewHeaderNoItem;

    PresenterAudioInMedia presenterAudioInMedia;

    //adapters
    AudioNewsAdapter audioNewsAdapter;

    //scrolling variables
    boolean isScrolling = false;
    boolean isLoading = false;

    int total_items, visible_items, scrollout_items;

    //TODO: Player Variables
    int musicPosition = -1;
    boolean isPrepared = true;
    Handler timer = new Handler();
    String currentTrackUrl = "https://muz11.z1.fm/e/ac/via_marokand_via_marokand_-_tarnov_tarnov_2016_(zf.fm).mp3";
    int musicDuration = -1;
    SimpleNewsModel currentModel;


    //#####################################################################

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterAudioInMedia = new PresenterAudioInMedia(this);

        if (getActivity() != null)
            isConnected_to_Net = MyUtil.hasConnectionToNet(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.window_audio_inside_media, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        canLoadMore = false;

        //initialize adapters and append to lists

        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.listAudio.setLayoutManager(manager);
        audioNewsAdapter = new AudioNewsAdapter(getContext(), new ArrayList<SimpleNewsModel>(), this);
        viewHeader = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.header_window_audio_inside_media,
                        binding.listAudio,
                        false);
        viewHeaderNoItem = LayoutInflater.from(getContext())
                .inflate(
                        R.layout.no_item_window_audio,
                        binding.listAudio,
                        false);

        audioNewsAdapter.withHeader(viewHeader);
        binding.listAudio.setAdapter(audioNewsAdapter);
        binding.getRoot().setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());
        binding.listAudio.setHasFixedSize(true);

        bindingNoConnection = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.window_no_connection, binding.listAudio, false);
        bindingFooter = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.footer_no_connection, binding.listAudio, false);


        //*****************************************************************************


        binding.swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        binding.swiper.setOnRefreshListener(this);
        binding.listAudio.addOnScrollListener(scrollListener);

        //TODO: localize here
        ((TextView) viewHeader.findViewById(R.id.text_title)).setText(MyUtil.getLocalizedString(getContext(), R.string.title_audio_news));
        ((TextView) viewHeader.findViewById(R.id.text_1)).setText(MyUtil.getLocalizedString(getContext(), R.string.messege_audio));
        ((TextView) viewHeaderNoItem.findViewById(R.id.text_title)).setText(MyUtil.getLocalizedString(getContext(), R.string.title_audio_news));
        ((TextView) viewHeaderNoItem.findViewById(R.id.text_1)).setText(MyUtil.getLocalizedString(getContext(), R.string.text_no_audio));
        //############################################################

//        binding.swiper.setRefreshing(true);
        presenterAudioInMedia.init();
    }

//TODO: override methods

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopPlayerAndTimer();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser == false) {
            stopPlayerAndTimer();
        }
    }

    @Override
    public void onRefresh() {
        isConnected_to_Net = true;
        if (isConnected_to_Net) {
            canLoadMore = false;
            stopPlayerAndTimer();
            presenterAudioInMedia.init();
        } else
            binding.swiper.setRefreshing(false);
    }

    //#################################################################


    //TODO: all methods from interface

    @Override
    public void playPressed(SimpleNewsModel m) {
        currentModel = m;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        timer.postDelayed(myTimerRunnable, 1000);

        EventBus.getDefault().post(new PlayerStateEvent(PLAYER_PAUSE_ICON, ""));
    }

    @Override
    public void pausePressed(SimpleNewsModel m) {
        currentModel = m;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

        timer.removeCallbacks(myTimerRunnable);
        EventBus.getDefault().post(new PlayerStateEvent(PLAYER_PLAY_ICON, ""));
    }

    @Override
    public void updateItems(SimpleNewsModel m, int positionTo) {
        if (musicPosition != -1)
            audioNewsAdapter.notifyItemChanged(musicPosition);
        musicPosition = positionTo;

        binding.listAudio.smoothScrollToPosition(positionTo);
        currentModel = m;
        currentTrackUrl = m.getUrlAudioFile();
        timer.removeCallbacks(myTimerRunnable);


        mediaPlayer = ((MyApplication) getActivity().getApplication()).getMyApplicationComponent().getMediaPlayer();
        mediaPlayer.reset();
        //        if (mediaPlayer != null) {
//            mediaPlayer.release();
//        }
        //mediaPlayer prepare listener
        mediaPlayer.setOnPreparedListener(prepareListenter);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(onCompletionListener);

        //TODO: Init media player

        try {
            mediaPlayer.setDataSource(currentTrackUrl);
            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }

        EventBus.getDefault().post(new PlayerStateEvent(PLAYER_RESET, ""));
        EventBus.getDefault().post(new PlayerStateEvent(PLAYER_TITLE, m.getTitle()));
    }

    @Override
    public void initAudio(List<SimpleNewsModel> items, int message) {
        binding.swiper.setRefreshing(false);
        audioNewsAdapter.isPlaying = false;
        audioNewsAdapter.playingMusicId = "";

        if (message == MESSAGE_NO_ITEMS) {
            audioNewsAdapter.withHeaderNoInternet(viewHeaderNoItem);
        } else if (message == MESSAGE_NO_CONNECTION) {
            audioNewsAdapter.withHeaderNoInternet(bindingNoConnection.getRoot());
        } else if (message == MESSAGE_OK) {
            audioNewsAdapter.withHeader(viewHeader);
            audioNewsAdapter.initItems(items);
            canLoadMore = true;
        }

    }


    @Override
    public void addAudio(List<SimpleNewsModel> items, int message) {

        audioNewsAdapter.hideLoading();
        isLoading = false;

        if (message == MESSAGE_NO_CONNECTION) {
            audioNewsAdapter.withFooter(bindingFooter.getRoot());
            return;
        }

        if (message == MESSAGE_OK) {
            audioNewsAdapter.addItems(items);
        }

    }


    //#################################################################

    //TODO: Additional methods

    public void stopPlayerAndTimer() {
        if (binding != null && binding.listAudio != null)
            binding.listAudio.setPadding(0, 0, 0, MySettings.getInstance().getNavigationHeight());
        if (audioNewsAdapter != null) {
            audioNewsAdapter.isPlaying = false;
            audioNewsAdapter.playingMusicId = "";
            if (musicPosition != -1)
                audioNewsAdapter.notifyItemChanged(musicPosition);
        }

        if (mediaPlayer != null) {
//            mediaPlayer.release();
            mediaPlayer.stop();
        }
        EventBus.getDefault().post(new PlayerStateEvent(PLAYER_HIDE, ""));
        timer.removeCallbacks(myTimerRunnable);
    }


    //#################################################################

    //TODO: implemented methods

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentTrackUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

        return false;
    }

    //#################################################################

    //TODO: From EVENTBUS

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_network_changed(NetworkStateChangedEvent event) {
        if (event.state == NETWORK_STATE_CONNECTED && !isConnected_to_Net) {
            binding.swiper.setRefreshing(false);
//            presenterAudioInMedia.init();
        }

        isConnected_to_Net = (event.state == NETWORK_STATE_CONNECTED);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void on_player_state_changed(PlayerStateEvent event) {
        switch (event.state) {
            case PLAYER_STOP:
                stopPlayerAndTimer();
                break;
            case PLAYER_PLAY:
                if (audioNewsAdapter.isPlaying) {
                    pausePressed(currentModel);
                } else
                    playPressed(currentModel);

                audioNewsAdapter.isPlaying = !audioNewsAdapter.isPlaying;
                audioNewsAdapter.notifyItemChanged(musicPosition);
                break;
            case PLAYER_PREV:
                audioNewsAdapter.prevMusic(currentModel.getNewsId());
                break;
            case PLAYER_NEXT:
                audioNewsAdapter.nextMusic(currentModel.getNewsId());
                break;
            case PLAYER_PROGRESS_CHANGED:
                if (isPrepared)
                    mediaPlayer.seekTo(
                            (int) (((double) musicDuration) * (Double.valueOf(event.value) / 100.0))
                    );
                SimpleDateFormat date = new SimpleDateFormat("mm:ss");
                String timeStart = date.format(new Date(mediaPlayer.getCurrentPosition()));
                String timeEnd = date.format(new Date(musicDuration));

                PlayerStateEvent event2 = new PlayerStateEvent(PLAYER_UPDATE_TIME,"");
                event2.startTime = timeStart;
                event2.endTime = timeEnd;
                EventBus.getDefault().post(event2);
                break;
            case PLAYER_OPENED_GET_HEIGHT:
                binding.listAudio.setPadding(0, 0, 0, Double.valueOf(event.value).intValue());

                break;
        }

    }
    //TODO: Argument variables

    boolean canLoadMore = false;
    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            Log.d(Constants.MY_LOG, "onScrollStateChanged");

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            total_items = manager.getItemCount();
            visible_items = manager.getChildCount();
            scrollout_items = manager.findFirstVisibleItemPosition();
            Log.d(Constants.MY_LOG, "total_items: " + total_items + " " +
                    "visible_items: " + visible_items + " " +
                    "scrollout_items: " + scrollout_items);

            if (isScrolling && (visible_items + scrollout_items == total_items) && !isLoading && canLoadMore) {
                isScrolling = false;
                isLoading = true;

                audioNewsAdapter.removeFooter();
                audioNewsAdapter.showLoading();
                presenterAudioInMedia.getContinue();
            }
        }

    };

    public MediaPlayer.OnPreparedListener prepareListenter = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            isPrepared = true;
            musicDuration = mediaPlayer.getDuration();
            mediaPlayer.start();
            if (timer != null) {
                timer.removeCallbacks(myTimerRunnable);
            }
            timer.postDelayed(myTimerRunnable, 1000);
            EventBus.getDefault().post(new PlayerStateEvent(PLAYER_RESET, ""));
        }
    };

    public MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(currentTrackUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
        }
    };

    public Runnable myTimerRunnable = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat date = new SimpleDateFormat("mm:ss");
            String timeStart = date.format(new Date(mediaPlayer.getCurrentPosition()));
            String timeEnd = date.format(new Date(musicDuration));
            String progress = String.valueOf(
                    (int) ((Double.valueOf(mediaPlayer.getCurrentPosition()) / Double.valueOf(musicDuration)) * 100)
            );

            PlayerStateEvent event = new PlayerStateEvent(PLAYER_UPDATE, progress);
            event.startTime = timeStart;
            event.endTime = timeEnd;
            EventBus.getDefault().post(event);

            timer.postDelayed(myTimerRunnable, 1000);
        }
    };

}