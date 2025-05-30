package com.conectatangara.activities; // Use seu pacote

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.conectatangara.R;
import com.conectatangara.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout da activity como activity_main.xml
        setContentView(R.layout.activity_main);

        // Verifica se é a primeira vez que a Activity está sendo criada
        // (evita adicionar o fragmento novamente ao girar a tela, por exemplo)
        if (savedInstanceState == null) {
            // Cria uma nova instância do HomeFragment
            HomeFragment homeFragment = new HomeFragment();

            // Usa o FragmentManager para iniciar uma transação,
            // substituir o conteúdo do 'main_fragment_container' pelo nosso HomeFragment,
            // e confirmar a transação.
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, homeFragment)
                    .commit();
        }
    }
}