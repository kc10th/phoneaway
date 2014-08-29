package com.rg.phone_away;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * @author YangJun
 * @date 2013 2013-12-19 下午4:44:59
 */
public class VoiceThread extends Thread {

	Context context;
	String fileName;
	private AudioManager audioMgr = null; // Audio管理器，控制音量
	int maxVolume = 50;
	int originVolume = 20;
	
	OnMusicFinishListener onMusicFinishListener;
	
	interface OnMusicFinishListener{
		public void onMusicFinish();
	}

	public VoiceThread(Context context, String filename) {
		this.context = context;
		this.fileName = filename;
		audioMgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		originVolume = audioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	public VoiceThread(Context context, String filename,
			OnMusicFinishListener onMusicFinishListener) {
		this(context, filename);
		this.onMusicFinishListener = onMusicFinishListener;
	}

	@Override
	public void run() {
		play();
		super.run();
	}

	private void play() {
		AssetManager am = context.getAssets();
		try {
			AssetFileDescriptor assetFileDescriptor = am.openFd(fileName);
			MediaPlayer mPlayer = new MediaPlayer();
			mPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
					assetFileDescriptor.getStartOffset(),
					assetFileDescriptor.getLength());
			mPlayer.prepare();
			setMaxVolume();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					resumeVolume();
					if(onMusicFinishListener != null){
						onMusicFinishListener.onMusicFinish();
					}
						
				}
			});
			mPlayer.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void setMaxVolume() {
		audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,
				AudioManager.FX_KEY_CLICK);
	}

	void resumeVolume() {
		audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, originVolume,
				AudioManager.FX_KEY_CLICK);
	}
}
