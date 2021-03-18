package dm2e.adriancaballero.laberinto;

import android.content.Context;
import android.media.MediaPlayer;

/**
 *
 * @author Adrian Caballeo Orasio
 */
public class Musica {
    private static MediaPlayer mp;

    public static MediaPlayer getMp() {
        return mp;
    }

    public static void setMediaPlayer() {
        mp = (mp == null) ? new MediaPlayer() : mp;
    }

    //Musica.musicaRaw(this, music);
    public static synchronized void musicaRaw(Context contexto, int id) throws IllegalArgumentException {
        setMediaPlayer();
        mp = MediaPlayer.create(contexto.getApplicationContext(), id);
        mp.start();
    }

    // public static synchronized void musicaSD(Context contexto, String pathSD) throws IllegalArgumentException, IOException { android.net.Uri datos = Uri.parse(Environment.getExternalStorageDirectory().getPath() + pathSD);setMediaPlayer();mp.setAudioStreamType(AudioManager.STREAM_MUSIC);mp.setDataSource(contexto.getApplicationContext(), datos);/* mp = MediaPlayer.create(contexto.getApplicationContext(), datos);*/mp.prepare();mp.start(); }
    // public static synchronized void musicaHTTP(String url) throws IllegalArgumentException, IOException { setMediaPlayer();mp.setAudioStreamType(AudioManager.STREAM_MUSIC);mp.setDataSource(url);mp.prepare();mp.start(); }
    // public static synchronized void musicaAssets(Context contexto, String asset) throws IllegalArgumentException, IOException { AssetFileDescriptor afd = contexto.getApplicationContext().getAssets().openFd(asset);setMediaPlayer();mp.reset();mp.setDataSource(afd.getFileDescriptor());mp.prepare();mp.start(); }

    public static void onPause() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }/*super.onPause();*/
    }

    public static void retirar() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}
