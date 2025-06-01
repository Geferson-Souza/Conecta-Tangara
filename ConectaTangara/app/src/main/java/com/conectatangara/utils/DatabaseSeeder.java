package com.conectatangara.utils;

import android.util.Log;

import com.conectatangara.models.Indicator;
import com.conectatangara.models.Ocorrencia;
// import com.conectatangara.models.Usuario; // Crie e importe se for popular usuários

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.WriteBatch;
// O import de com.google.firebase.Timestamp não é mais necessário aqui
// se estivermos usando @ServerTimestamp nos modelos e passando java.util.Date aos setters (se necessário).

import java.util.Arrays;
import java.util.Date; // Usaremos java.util.Date para os setters se não usar @ServerTimestamp
import java.util.HashMap;
import java.util.Map;
// import java.util.concurrent.TimeUnit; // Não mais necessário se @ServerTimestamp cuida das datas

public class DatabaseSeeder {

    private static final String TAG = "DatabaseSeeder";

    private static final String USER_ID_CIDADAO_1 = "uid_cidadao_exemplo_001";
    private static final String USER_ID_CIDADAO_2 = "uid_cidadao_exemplo_002";
    private static final String USER_ID_FUNCIONARIO_1 = "uid_funcionario_exemplo_001";

    public static void seedDatabase(FirebaseFirestore db) {
        if (db == null) {
            Log.e(TAG, "Instância do FirebaseFirestore é nula.");
            return;
        }
        WriteBatch batch = db.batch();
        Log.d(TAG, "Iniciando o seeding do banco de dados Firestore...");

        // --- Seed Usuários (Opcional) ---
        /*
        Map<String, Object> user1Data = new HashMap<>();
        user1Data.put("nomeCompleto", "Ana Silva (Cidadã)");
        user1Data.put("email", "ana.silva@example.com");
        user1Data.put("tipoUsuario", "cidadao");
        // dataCadastro será preenchido pelo @ServerTimestamp se você adicionar esse campo ao modelo Usuario
        // user1Data.put("dataCadastro", new Date());
        batch.set(db.collection("usuarios").document(USER_ID_CIDADAO_1), user1Data);
        // ... mais usuários ...
        */

        // --- Seed Ocorrências ---
        try {
            Ocorrencia oc1 = new Ocorrencia();
            oc1.setUserId(USER_ID_CIDADAO_1);
            oc1.setTitulo("Buraco perigoso na Avenida Brasil");
            oc1.setDescricao("Um buraco de tamanho considerável abriu na Rua Principal, esquina com a Av. Brasil. Risco de acidentes.");
            oc1.setCategoria("Buraco na Via");
            oc1.setStatus("Recebida");
            oc1.setLocalizacao(new GeoPoint(-14.620123, -57.490456));
            oc1.setEnderecoTexto("Avenida Brasil, perto do nº 100"); // Adicionado
            oc1.setBairro("Centro");
            // dataRegistro e dataUltimaAtualizacao serão preenchidos pelo @ServerTimestamp no modelo Ocorrencia
            oc1.setMediaUrls(Arrays.asList("https://via.placeholder.com/300/E63946/FFFFFF?Text=FotoBuraco1.jpg"));
            batch.set(db.collection("ocorrencias").document("ocorrencia_exemplo_001"), oc1);

            Ocorrencia oc2 = new Ocorrencia();
            oc2.setUserId(USER_ID_CIDADAO_2);
            oc2.setTitulo("Poste apagado na Praça da Matriz");
            oc2.setDescricao("Iluminação pública com defeito, o poste principal da Praça da Matriz está completamente apagado há 3 noites.");
            oc2.setCategoria("Lâmpada Queimada/Defeito na Iluminação");
            oc2.setStatus("Em Análise");
            oc2.setLocalizacao(new GeoPoint(-14.625555, -57.495111));
            oc2.setEnderecoTexto("Praça da Matriz, em frente à igreja"); // Adicionado
            oc2.setBairro("Centro");
            oc2.setAtribuidoPara(USER_ID_FUNCIONARIO_1);
            batch.set(db.collection("ocorrencias").document("ocorrencia_exemplo_002"), oc2);

            Ocorrencia oc3 = new Ocorrencia();
            oc3.setUserId(USER_ID_CIDADAO_1);
            oc3.setTitulo("Coleta de lixo irregular na Vila Alta");
            oc3.setDescricao("A coleta de lixo no bairro Vila Alta não passou nos últimos dois dias programados.");
            oc3.setCategoria("Lixo Acumulado/Problema na Coleta");
            oc3.setStatus("Em Andamento");
            oc3.setLocalizacao(new GeoPoint(-14.630222, -57.500333));
            oc3.setEnderecoTexto("Rua das Flores, esquina com Rua das Margaridas, Vila Alta"); // Adicionado
            oc3.setBairro("Vila Alta");
            oc3.setAtribuidoPara(USER_ID_FUNCIONARIO_1);
            oc3.setResolucaoFuncionario("Equipe notificada, coleta será normalizada amanhã.");
            batch.set(db.collection("ocorrencias").document("ocorrencia_exemplo_003"), oc3);

            // Adicione mais ocorrências...

        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar ocorrências de exemplo", e);
        }

        // --- Seed Indicadores ---
        try {
            // Certifique-se que o construtor de Indicator está correto (id, name, value, source, period, bairro, tema)
            Indicator ind1 = new Indicator("ind_taxa_resol_geral", "Taxa de Resolução Geral", "82%", "Painel Interno", "2024", "Todos", "Geral");
            ind1.setChartType("BARRA");
            batch.set(db.collection("indicadores").document(ind1.getId()), ind1);

            Indicator ind2 = new Indicator("ind_ocorrencias_saude", "Ocorrências de Saúde Recebidas", "45", "Sistema Conecta", "Últimos 30 dias", "Todos", "Saúde");
            batch.set(db.collection("indicadores").document(ind2.getId()), ind2);

            // Adicione mais indicadores...

        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar indicadores de exemplo", e);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Banco de dados Firestore populado com sucesso!"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao popular o banco de dados Firestore.", e));
    }
}
