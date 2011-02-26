/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.list;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.binding.LibX11;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.player.list.MediaList;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

/**
 * Example showing how to combine a media list player with an embedded media
 * player.
 */
public class TestMediaListEmbeddedPlayer {

  public static void main(String[] args) throws Exception {
    LibX11.INSTANCE.XInitThreads();
    
    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();

    Canvas canvas = new Canvas();
    canvas.setBackground(Color.black);
    CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
    
    EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
    mediaPlayer.setVideoSurface(videoSurface);
    
    MediaListPlayer mediaListPlayer = mediaPlayerFactory.newMediaListPlayer();
    
    mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
      @Override
      public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
        System.out.println("nextItem()");
      }
    });
    
    mediaListPlayer.setMediaPlayer(mediaPlayer);
    
    JPanel cp = new JPanel();
    cp.setBackground(Color.black);
    cp.setLayout(new BorderLayout());
    cp.add(canvas, BorderLayout.CENTER);
    
    JFrame f = new JFrame("vlcj embedded media list player test");
    f.setContentPane(cp);
    f.setSize(800, 600);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setVisible(true);
    
    MediaList mediaList = mediaPlayerFactory.newMediaList();
    mediaList.addMedia("/movies/1.mp4");
    mediaList.addMedia("/movies/2.mp4");
    mediaList.addMedia("/movies/3.mp4");
    
    mediaListPlayer.setMediaList(mediaList);
    mediaListPlayer.setMode(MediaListPlayerMode.LOOP);
    
    // FIXME Unfortunately because of the way the media player works, you have 
    //       to call play here even though it does nothing (except set the 
    //       video surface)
    mediaPlayer.play(); 
    
    mediaListPlayer.play();

    for(;;) {
      Thread.sleep(500);
      mediaPlayer.setChapter(3);
      
      Thread.sleep(5000);
      mediaListPlayer.playNext();
    }
    
//    mediaList.release();
//    mediaListPlayer.release();
//    mediaPlayer.release();
//    mediaPlayerFactory.release();
  }
}