package dm2e.laberinto;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * @author Adrian Caballeo Orasio
 */
public class MainActivity extends AppCompatActivity {
    private static final int MAZE_PLAYER_TEXT_ACTIVITY_TAG = 1;
    private static final int MAZE_PLAYER_DRAW_ACTIVITY_TAG = 2;
    private final String TAG = getClass().getName();
    private String laberinto = "";
    private String modo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        actualizarBD((TextView) findViewById(R.id.tvGanardores));
        makMazes(R.id.rgMazes, 4, LinearLayout.VERTICAL);
        makeModes(R.id.rgModes, 2, LinearLayout.VERTICAL);
    }

//    public int findId() { View v = findViewById(ids);while (v != null) v = findViewById(++ids);return ids++; }private static int ids = 1;

    public void makMazes(@IdRes int rgId, int modulo, int orientation) {
        RadioGroup rg = findViewById(rgId);
        rg.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        rg.setOrientation(orientation);
        rg.setGravity(Gravity.CENTER);
        LinearLayout ll = null;
        for (int i = 1; getResourceId("m" + i, "raw") != 0; i++) {
            if ((i - 1) % modulo == 0) {
                if (ll != null) {
                    rg.addView(ll);
                }
                ll = new LinearLayout(this);
                ll.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                ll.setOrientation((rg.getOrientation() != LinearLayout.VERTICAL) ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
                ll.setGravity(Gravity.CENTER);
            }
            RadioButton rb = new RadioButton(this);
            String txt = "m" + i;//getString((int) getResourceId("m" + i, "raw"));
            rb.setText(txt);
            rb.setTag(txt);

//            android:layout_weight="50" android:checked="false" android:gravity="center"

            rb.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
            rb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, "onClick Mazes." + v.getTag() + ": " + v.getId()
                            + " : " + ((RadioButton) v).getText());
                    RadioGroup rgMazes = (RadioGroup) v.getParent().getParent();//findViewById(R.id.rgMazes);
                    for (int i = 0; i < rgMazes.getChildCount(); i++) {
                        LinearLayout ll = (LinearLayout) rgMazes.getChildAt(i);
                        for (int j = 0; j < ll.getChildCount(); j++) {
                            if (ll.getChildAt(j) != v)
                                ((RadioButton) ll.getChildAt(j)).setChecked(false);
                        }
                    }
                    if (v.getTag() != null) laberinto = (String) v.getTag();
                    else laberinto = "";
                }
            });
            ll.addView(rb);
        }
        if (ll != null) {
            rg.addView(ll);
        }
    }

    public void makeModes(@IdRes int rgId, int modulo, int orientation) {
        RadioGroup rg = findViewById(rgId);
        rg.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        rg.setOrientation(orientation);
        rg.setGravity(Gravity.CENTER);
        String[] modos = getResources().getStringArray(R.array.modos);
        LinearLayout ll = null;
        for (int i = 0; i < modos.length; i++) {
            if (i % modulo == 0) {
                if (ll != null) {
                    rg.addView(ll);
                }
                ll = new LinearLayout(this);
                ll.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                ll.setOrientation((rg.getOrientation() != LinearLayout.VERTICAL) ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
                ll.setGravity(Gravity.CENTER);
            }
            RadioButton rb = new RadioButton(this);
            rb.setText(modos[i]);
            rb.setTag(modos[i]);
            rb.setLayoutParams(new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
            rb.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, "onClick Modes." + v.getTag() + ": " + v.getId());
                    RadioGroup rgModes = (RadioGroup) v.getParent().getParent();//findViewById(R.id.rgModes);
                    for (int i = 0; i < rgModes.getChildCount(); i++) {
//                        if (rgModes.getChildAt(i) != v) ((RadioButton) rgModes.getChildAt(i)).setChecked(false);
                        LinearLayout ll = (LinearLayout) rgModes.getChildAt(i);
                        for (int j = 0; j < ll.getChildCount(); j++) {
                            if (ll.getChildAt(j) != v)
                                ((RadioButton) ll.getChildAt(j)).setChecked(false);
                        }
                    }
                    if (v.getTag() != null) modo = (String) v.getTag();
                    else modo = "";
                }
            });
            ll.addView(rb);
        }
        if (ll != null) {
            rg.addView(ll);
        }
    }

    public void onPulsar(View v) {
        String nombre = ((EditText) findViewById(R.id.etNombre)).getText().toString().trim();
        if (nombre.equals("")) muestraMensaje(R.string.nombreInvalido);
        else if (this.laberinto.equals("")) muestraMensaje(R.string.laberintoInvalido);
//        else if (modo.equals("")){ muestraMensaje(R.string.modoInvalido); }
        else {
            /*witch (R.string) {}*/
            if (modo.equals(getString(R.string.modo_texto))) {
                startIntent(nombre, MazePlayerTextActivity.class, MAZE_PLAYER_TEXT_ACTIVITY_TAG);
            } else if (modo.equals(getString(R.string.modo_dibujo))) {
                startIntent(nombre, MazePlayerDrawActivity.class, MAZE_PLAYER_DRAW_ACTIVITY_TAG);
            } else muestraMensaje(R.string.modoInvalido);
        }
    }

    public void startIntent(String nombre, Class clase, int activityTag) {
        Intent intent = new Intent(this, clase);
        intent.putExtra("nombre", nombre);
        intent.putExtra("laberinto", this.laberinto);
        startActivityForResult(intent, activityTag);
    }

    /***********************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        /*new Handler().post(new Runnable() {@Override public void run() { final View menuItemView = findViewById(R.id.popup);openPopup(menuItemView); }}); //*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings: {
                return true;
            }
            case R.id.menu_ranking: {
                final View v = findViewById(R.id.menu_settings);/*item.getActionView()*/
                openPopup(v, R.id.menu_ranking);
                return true;
            }
            case R.id.menu_help: {
                final View v = findViewById(R.id.menu_settings);/*item.getActionView()*/
                openPopup(v, R.id.menu_help);
                return true;
            }
            case R.id.menu_info: {
                final View v = findViewById(R.id.menu_settings);/*item.getActionView()*/
                openPopup(v, R.id.menu_info);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void openPopup(View view) {
        openPopup(view, view.getId());
    }

    public void openPopup(View view, /*@IdRes*/ int idPopup) {
        if (view == null) {
            muestraMensaje("Error al abrir poup");
        }
        int width = LayoutParams.WRAP_CONTENT, height = LayoutParams.WRAP_CONTENT;
        LayoutInflater lInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.popup_window, null, false), 100, 100, true);

        View popupView = lInflater.inflate(R.layout.popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView tv = (TextView) popupView.findViewById(R.id.tv_popup);
        View accept = popupView.findViewById(R.id.accept_popup);
        View cancel = popupView.findViewById(R.id.cancel_popup);

        switch (idPopup) {
            case R.id.menu_settings: {
                break;
            }
            case R.id.popup_ranking:
            case R.id.menu_ranking: {
                actualizarBD(tv);
                tv.setGravity(Gravity.NO_GRAVITY);
                accept.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.popup_help:
            case R.id.menu_help: {
                tv.setText(getSpannedText(getString(R.string.popup_help)));
                tv.setGravity(Gravity.NO_GRAVITY);
                accept.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.popup_info:
            case R.id.menu_info: {
                tv.setText(getSpannedText(getString(R.string.popup_informacion)));
                tv.setGravity(Gravity.CENTER);
//                tv.setText(R.string.informacion);
                accept.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                break;
            }

        }
//        if(accept.getVisibility() == View.VISIBLE) {accept.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { }});}
        if (cancel.getVisibility() == View.VISIBLE) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        } else {
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    return true;
                }
            });
        }
    }

    private Spanned getSpannedText(String text) {
        if (VERSION.SDK_INT < VERSION_CODES.N) return Html.fromHtml(text);
        else return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
    }//public static CharSequence getText(Context context, int id, Object... args) { for (int i = 0; i < args.length; ++i) args[i] = (args[i] instanceof String) ? TextUtils.htmlEncode((String) args[i]) : args[i];return Html.fromHtml(String.format(Html.toHtml(new SpannedString(context.getText(id))), args)); }

    /***********************************************************************************************/
//    public void selectLaberinto(View v) { Log.i(TAG, "selectLaberinto raw.m" + v.getTag() + ": " + v.getId());if (v.getId() == R.id.laberinto1) this.laberinto = getString(R.string.laberinto_1);else if (v.getId() == R.id.laberinto2) this.laberinto = getString(R.string.laberinto_2);else if (v.getId() == R.id.laberinto3) this.laberinto = getString(R.string.laberinto_3);else if (v.getId() == R.id.laberinto4) this.laberinto = getString(R.string.laberinto_4);else this.laberinto = ""; }
//    public void selectModo(View v) { Log.i(TAG, "onClick StringArray.modo." + v.getTag() + ": " + v.getId());if (v.getId() == R.id.modo1) this.modo = getString(R.string.modo_texto);else if (v.getId() == R.id.modo2) this.modo = getString(R.string.modo_dibujo);else this.modo = ""; }
    protected void actualizarBD(TextView tv) {
        try {
            SQLiteHelper_Ranking db = SQLiteHelper_Ranking.getInstance(this);
            List<Ranking> ranking = db.getAllRankings();
            String txt = "";
            for (Ranking r : ranking) {
                txt = String.format("%s%s<br>", txt, r.toString(this));
            }
            tv.setText(getSpannedText(getString(R.string.rankings, txt)));
        } catch (Exception e) {
            muestraMensaje(R.string.errorGanardores);
            e.printStackTrace();
        }
    }

    protected void muestraMensaje(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

    protected void muestraMensaje(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

    public int getResourceId(String name, String defType) {
        return getResources().getIdentifier(name, defType, getPackageName());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);/*String respuesta = data.getStringExtra("respuesta");*//*if(resultCode==RESULT_FIRST_USER){}else if(resultCode==RESULT_OK){}else if(resultCode==RESULT_CANCELED){}*/
        onPause();
//        actualizarBD((TextView) findViewById(R.id.tvGanardores));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Musica.getMp() != null && Musica.getMp().isPlaying()) {
            Musica.onPause();
            Musica.retirar();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}