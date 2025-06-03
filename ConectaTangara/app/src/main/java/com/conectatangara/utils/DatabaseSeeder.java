package com.conectatangara.utils;

import android.util.Log;

import com.conectatangara.models.Indicator;
import com.conectatangara.models.Ocorrencia;
// import com.conectatangara.models.Usuario; // Crie esta classe se for popular usuários

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List; // Para List
import java.util.ArrayList; // Para ArrayList

public class DatabaseSeeder {

    private static final String TAG = "DatabaseSeeder";

    // Exemplos de UIDs (substitua por UIDs reais do Firebase Auth se os tiver para teste)
    private static final String USER_ID_ANA = "uid_ana_silva_001";
    private static final String USER_ID_BRUNO = "uid_bruno_costa_002";
    private static final String USER_ID_CARLA = "uid_carla_dias_003";
    private static final String USER_ID_FUNCIONARIO_JOAO = "uid_joao_funcionario_001";
    private static final String USER_ID_FUNCIONARIO_MARIA = "uid_maria_funcionario_002";


    public static void seedDatabase(FirebaseFirestore db) {
        if (db == null) {
            Log.e(TAG, "Instância do FirebaseFirestore é nula. Não é possível popular o banco.");
            return;
        }
        WriteBatch batch = db.batch();
        Log.d(TAG, "Iniciando o seeding do banco de dados Firestore com dados expandidos...");

        // --- Seed Usuários (Opcional - Crie uma classe Usuario em com.conectatangara.models) ---
        // Se você criar a classe Usuario, descomente e ajuste.
        // Certifique-se que os UIDs aqui correspondem aos que você criou no Firebase Authentication para teste.
        /*
        Map<String, Object> userAna = new HashMap<>();
        userAna.put("nomeCompleto", "Ana Silva Pereira");
        userAna.put("email", "ana.silva@example.com");
        userAna.put("tipoUsuario", "cidadao");
        // dataCadastro será preenchido pelo @ServerTimestamp se você adicionar esse campo ao modelo Usuario
        batch.set(db.collection("usuarios").document(USER_ID_ANA), userAna);

        Map<String, Object> userBruno = new HashMap<>();
        userBruno.put("nomeCompleto", "Bruno Costa Lima");
        userBruno.put("email", "bruno.costa@example.com");
        userBruno.put("tipoUsuario", "cidadao");
        batch.set(db.collection("usuarios").document(USER_ID_BRUNO), userBruno);

        Map<String, Object> userCarla = new HashMap<>();
        userCarla.put("nomeCompleto", "Carla Dias Oliveira");
        userCarla.put("email", "carla.dias@example.com");
        userCarla.put("tipoUsuario", "cidadao");
        batch.set(db.collection("usuarios").document(USER_ID_CARLA), userCarla);


        Map<String, Object> funcJoao = new HashMap<>();
        funcJoao.put("nomeCompleto", "João Alves (Obras)");
        funcJoao.put("email", "joao.alves@tangaradaserra.mt.gov.br");
        funcJoao.put("tipoUsuario", "funcionario");
        batch.set(db.collection("usuarios").document(USER_ID_FUNCIONARIO_JOAO), funcJoao);

        Map<String, Object> funcMaria = new HashMap<>();
        funcMaria.put("nomeCompleto", "Maria Santos (Iluminação)");
        funcMaria.put("email", "maria.santos@tangaradaserra.mt.gov.br");
        funcMaria.put("tipoUsuario", "funcionario");
        batch.set(db.collection("usuarios").document(USER_ID_FUNCIONARIO_MARIA), funcMaria);
        */

        // --- Seed Ocorrências ---
        try {
            String[] bairrosTangara = {"Centro", "Vila Alta", "Jardim Europa", "Parque Industrial", "Jardim Califórnia", "Progresso", "Vila Esmeralda", "Jardim dos Ipês", "Jardim Itália", "Santa Lúcia"};
            String[] categorias = {"Buraco na Via", "Lâmpada Queimada/Defeito na Iluminação", "Lixo Acumulado/Problema na Coleta", "Vazamento de Água ou Esgoto", "Semáforo com Defeito/Problema de Sinalização", "Calçada Irregular ou Obstruída", "Problema em Ponto de Ônibus"};
            String[] statusOcorrencia = {"Recebida", "Em Análise", "Em Andamento", "Resolvida", "Rejeitada"};
            String[] userIdsCidadaos = {USER_ID_ANA, USER_ID_BRUNO, USER_ID_CARLA};
            String[] userIdsFuncionarios = {USER_ID_FUNCIONARIO_JOAO, USER_ID_FUNCIONARIO_MARIA};

            // Coordenadas aproximadas do centro de Tangará da Serra: Lat -14.6195, Long -57.4895
            // Vamos gerar ocorrências em um raio aproximado
            double baseLat = -14.6195;
            double baseLng = -57.4895;

            for (int i = 1; i <= 20; i++) { // Gerar 20 ocorrências
                Ocorrencia oc = new Ocorrencia();
                oc.setUserId(userIdsCidadaos[i % userIdsCidadaos.length]);
                oc.setCategoria(categorias[i % categorias.length]);
                oc.setStatus(statusOcorrencia[i % statusOcorrencia.length]);
                oc.setBairro(bairrosTangara[i % bairrosTangara.length]);

                // Gerar coordenadas aleatórias próximas
                double latOffset = (Math.random() - 0.5) * 0.05; // Variação de ~5km
                double lngOffset = (Math.random() - 0.5) * 0.05;
                oc.setLocalizacao(new GeoPoint(baseLat + latOffset, baseLng + lngOffset));

                oc.setTitulo(oc.getCategoria() + " no bairro " + oc.getBairro() + " #" + i);
                oc.setDescricao("Descrição detalhada da ocorrência de " + oc.getCategoria() + " número " + i + " localizada no bairro " + oc.getBairro() + ". Problema reportado para análise e resolução pelas equipes competentes.");
                oc.setEnderecoTexto("Rua Exemplo " + i + ", " + oc.getBairro() + ", Tangará da Serra - MT");

                if (Math.random() > 0.5) { // 50% chance de ter mídia
                    oc.setMediaUrls(Arrays.asList("https://via.placeholder.com/300/0000FF/FFFFFF?Text=MidiaExemplo" + i + ".jpg"));
                }

                if (oc.getStatus().equals("Em Andamento") || oc.getStatus().equals("Resolvida") || oc.getStatus().equals("Rejeitada")) {
                    oc.setAtribuidoPara(userIdsFuncionarios[i % userIdsFuncionarios.length]);
                    if (oc.getStatus().equals("Resolvida") || oc.getStatus().equals("Rejeitada")) {
                        oc.setResolucaoFuncionario("Status atualizado pela equipe técnica. Ocorrência " + oc.getStatus().toLowerCase() + ".");
                    }
                }
                // dataRegistro e dataUltimaAtualizacao serão preenchidos pelo @ServerTimestamp
                batch.set(db.collection("ocorrencias").document("ocorrencia_ficticia_" + String.format("%03d", i)), oc);
            }

        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar ocorrências de exemplo", e);
        }

        // --- Seed Indicadores ---
        try {
            Indicator ind1 = new Indicator("taxa_alfabetizacao_2023", "Taxa de Alfabetização (Municipal)", "97.5%", "Secretaria de Educação", "2023", "Todos", "Educação");
            ind1.setChartType("BARRA");
            batch.set(db.collection("indicadores").document(ind1.getId()), ind1);

            Indicator ind2 = new Indicator("leitos_uti_disponiveis_jun25", "Leitos de UTI Disponíveis", "12", "Secretaria de Saúde", "Junho/2025", "Todos", "Saúde");
            batch.set(db.collection("indicadores").document(ind2.getId()), ind2);

            Indicator ind3 = new Indicator("km_asfalto_novo_2024", "Km de Asfalto Novo", "15 km", "Secretaria de Obras", "2024", "Todos", "Infraestrutura");
            ind3.setChartType("LINHA");
            batch.set(db.collection("indicadores").document(ind3.getId()), ind3);

            Indicator ind4 = new Indicator("indice_coleta_seletiva_centro", "Índice de Coleta Seletiva", "65%", "Serviços Urbanos", "Maio/2025", "Centro", "Infraestrutura");
            ind4.setChartType("PIZZA");
            batch.set(db.collection("indicadores").document(ind4.getId()), ind4);

            Indicator ind5 = new Indicator("ocorrencias_furto_vilaalta_2024", "Ocorrências de Furto (Vila Alta)", "8", "Polícia Militar", "2024 (YTD)", "Vila Alta", "Segurança");
            batch.set(db.collection("indicadores").document(ind5.getId()), ind5);

        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar indicadores de exemplo", e);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Banco de dados Firestore populado com dados expandidos!"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao popular o banco de dados Firestore com dados expandidos.", e));
    }
}
