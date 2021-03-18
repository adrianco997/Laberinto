package dm2e.adriancaballero.laberinto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        actualizarBD();
//        RadioGroup rg = (RadioGroup) findViewById(R.id.ll1);for (int i = 1, mazeId = getResourceId("m" + i, "raw"); mazeId != 0; i++, mazeId = getResourceId("m" + i, "raw")) { Log.println(Log.ASSERT, TAG, "raw.m" + i + ": " + mazeId);RadioButton rb = new RadioButton(this);rb.setText(getResourceId("laberinto_" + i, "string"));rb.setTag(getResourceId("laberinto_" + i, "string"));rg.setOnClickListener(new View.OnClickListener() {public void onClick(View v) { Log.println(Log.ASSERT, TAG, "onClick raw.m" + v.getTag() + ": " + v.getId());RadioGroup rg = (RadioGroup) findViewById(R.id.ll1);for (int i = 0; i < rg.getChildCount(); i++) { if (rg.getChildAt(i) != v) ((RadioButton) rg.getChildAt(i)).setChecked(false); }if (v.getTag() != null) this.laberinto = (String) v.getTag();else this.laberinto = ""; }});rg.addView(rb); }
    }
//    private static int ids = 1;
//    public int findId() { View v = findViewById(ids);while (v != null) v = findViewById(++ids);return ids++; }

    public void onPulsar(View v) {
        String nombre = ((EditText) findViewById(R.id.etNombre)).getText().toString().trim();
        if (nombre.equals("")) muestraMensaje(R.string.nombreInvalido);
        else if (this.laberinto.equals("")) muestraMensaje(R.string.laberintoInvalido);
//        else if (modo.equals("")){ }muestraMensaje(R.string.modoInvalido); }
        else {

            if (modo.equals(getString(R.string.modo_texto))) {
                Intent intent = new Intent(this, MazePlayerTextActivity.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("laberinto", this.laberinto);
                startActivityForResult(intent, MAZE_PLAYER_TEXT_ACTIVITY_TAG);
            }
            else if (modo.equals(getString(R.string.modo_dibujo))) {
                Intent intent = new Intent(this, MazePlayerDrawActivity.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("laberinto", this.laberinto);
                startActivityForResult(intent, MAZE_PLAYER_DRAW_ACTIVITY_TAG);
            }
            else muestraMensaje(R.string.modoInvalido);

        }
    }
    /*// Inflate the menu; this adds items to the action bar if it is present.
    @Override public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.menu_main, menu);return true; }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) { return true; }
        return super.onOptionsItemSelected(item);
    }*/
    public void selectLaberinto(View v) {
        if (v.getId() == R.id.laberinto1) this.laberinto = getString(R.string.laberinto_01);
        else if (v.getId() == R.id.laberinto2) this.laberinto = getString(R.string.laberinto_02);
        else if (v.getId() == R.id.laberinto3) this.laberinto = getString(R.string.laberinto_03);
        else if (v.getId() == R.id.laberinto4) this.laberinto = getString(R.string.laberinto_04);
        else this.laberinto = "";
    }

    public void selectModo(View v) {
        if (v.getId() == R.id.modo1) this.modo = getString(R.string.modo_texto);
        else if (v.getId() == R.id.modo2) this.modo = getString(R.string.modo_dibujo);
        else this.modo = "";
    }


    protected void actualizarBD() {
        String txt = "Rankings:\n";
        try {
            SQLiteHelper_Ranking db = SQLiteHelper_Ranking.getInstance(this);
            List<Ranking> ranking = db.getAllRankings();
            for (Ranking r : ranking) {
                txt = String.format("%s%s\n", txt, r.toString(this));

//                String text = getString(R.string.welcome_messages, username, mailCount);
//                Spanned styledText = Html.fromHtml(text, FROM_HTML_MODE_LEGACY);
            }
            ((TextView) findViewById(R.id.tvGanardores)).setText(txt);
        } catch (Exception e) {
            muestraMensaje(R.string.errorGanardores);
        }
    }


    //    protected void muestraMensaje(String txt, int duration) { Toast.makeText(this, txt, duration).show(); }
    protected void muestraMensaje(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }
    //public int getResourceId(String name, String defType) { return getResources().getIdentifier(name, defType, getPackageName()); }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);/*String respuesta = data.getStringExtra("respuesta");*//*if(resultCode==RESULT_FIRST_USER){}else if(resultCode==RESULT_OK){}else if(resultCode==RESULT_CANCELED){}*/
////        ((RadioButton) findViewById(R.id.laberinto1)).setChecked(false);((RadioButton) findViewById(R.id.laberinto2)).setChecked(false);((RadioButton) findViewById(R.id.laberinto3)).setChecked(false);((RadioButton) findViewById(R.id.laberinto4)).setChecked(false);
//        { RadioGroup rg1 = findViewById(R.id.rg1);int rbId1 = rg1.getCheckedRadioButtonId();RadioButton rb1 = findViewById(rbId1);rb1.setChecked(false);this.laberinto = ""; }
//        { RadioGroup rg2 = findViewById(R.id.rg2);int rbId2 = rg2.getCheckedRadioButtonId();RadioButton rb2 = findViewById(rbId2);rb2.setChecked(false);this.modo = ""; }
        onPause();
        actualizarBD();
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