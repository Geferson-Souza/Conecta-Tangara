package com.conectatangara.utils;

import android.util.Log;

import com.conectatangara.models.Indicator;
import com.conectatangara.models.Ocorrencia;
// import com.conectatangara.models.Usuario; // Descomente quando criar a classe Usuario

import com.google.firebase.Timestamp; // Para datas, se não usar @ServerTimestamp direto no modelo para o seeder
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DatabaseSeeder {

    private static final String TAG = "DatabaseSeeder";

    // IDs de usuário de exemplo (para UIDs reais, crie usuários no Firebase Auth e copie os UIDs)
    private static final String USER_ID_ANA = "uid_ana_silva_001_ficticio";
    private static final String USER_ID_BRUNO = "uid_bruno_costa_002_ficticio";
    private static final String USER_ID_CARLA = "uid_carla_dias_003_ficticio";
    private static final String USER_ID_DANIEL = "uid_daniel_fernandes_004_ficticio";
    private static final String USER_ID_ELISA = "uid_elisa_gomes_005_ficticio";

    private static final String USER_ID_FUNCIONARIO_JOAO = "uid_joao_obras_func_001_ficticio";
    private static final String USER_ID_FUNCIONARIO_MARIA = "uid_maria_iluminacao_func_002_ficticio";
    private static final String USER_ID_FUNCIONARIO_PEDRO = "uid_pedro_saude_func_003_ficticio";
    private static final String USER_ID_GESTOR_SOFIA = "uid_sofia_gestora_004_ficticio";


    public static void seedDatabase(FirebaseFirestore db) {
        if (db == null) {
            Log.e(TAG, "Instância do FirebaseFirestore é nula. Não é possível popular o banco.");
            return;
        }
        WriteBatch batch = db.batch();
        Log.d(TAG, "Iniciando o seeding do banco de dados Firestore com dados expandidos...");

        // --- Seed Usuários (Opcional - Crie uma classe Usuario em com.conectatangara.models) ---
        // Para este seeder funcionar independentemente da criação manual no Auth, vamos usar UIDs fictícios.
        // Se você criar um modelo Usuario.java, pode usar `batch.set(db.collection("usuarios").document(USER_ID_ANA), new Usuario(...));`

        Map<String, Object> userAna = new HashMap<>();
        userAna.put("nomeCompleto", "Ana Silva Pereira");
        userAna.put("email", "ana.silva@example.com");
        userAna.put("tipoUsuario", "cidadao");
        userAna.put("dataCadastro", Timestamp.now()); // Usando Timestamp do Firebase para consistência com o Firestore
        // userAna.put("fotoUrl", "url_da_foto_ana.jpg"); // Adicionar se tiver URLs de placeholder
        batch.set(db.collection("usuarios").document(USER_ID_ANA), userAna);

        Map<String, Object> userBruno = new HashMap<>();
        userBruno.put("nomeCompleto", "Bruno Costa Lima");
        userBruno.put("email", "bruno.costa@example.com");
        userBruno.put("tipoUsuario", "cidadao");
        userBruno.put("dataCadastro", Timestamp.now());
        batch.set(db.collection("usuarios").document(USER_ID_BRUNO), userBruno);

        Map<String, Object> userCarla = new HashMap<>();
        userCarla.put("nomeCompleto", "Carla Dias Oliveira");
        userCarla.put("email", "carla.dias@example.com");
        userCarla.put("tipoUsuario", "cidadao");
        userCarla.put("dataCadastro", Timestamp.now());
        batch.set(db.collection("usuarios").document(USER_ID_CARLA), userCarla);

        Map<String, Object> userDaniel = new HashMap<>();
        userDaniel.put("nomeCompleto", "Daniel Fernandes Alves");
        userDaniel.put("email", "daniel.fernandes@example.com");
        userDaniel.put("tipoUsuario", "cidadao");
        userDaniel.put("dataCadastro", Timestamp.now());
        batch.set(db.collection("usuarios").document(USER_ID_DANIEL), userDaniel);

        Map<String, Object> userElisa = new HashMap<>();
        userElisa.put("nomeCompleto", "Elisa Gomes Ribeiro");
        userElisa.put("email", "elisa.gomes@example.com");
        userElisa.put("tipoUsuario", "cidadao");
        userElisa.put("dataCadastro", Timestamp.now());
        batch.set(db.collection("usuarios").document(USER_ID_ELISA), userElisa);

        Map<String, Object> funcJoao = new HashMap<>();
        funcJoao.put("nomeCompleto", "João Alves (Obras)");
        funcJoao.put("email", "joao.alves@tangaradaserra.mt.gov.br");
        funcJoao.put("tipoUsuario", "funcionario");
        funcJoao.put("dataCadastro", Timestamp.now());
        batch.set(db.collection("usuarios").document(USER_ID_FUNCIONARIO_JOAO), funcJoao);

        Map<String, Object> funcMaria = new HashMap<>();
        funcMaria.put("nomeCompleto", "Maria Santos (Iluminação)");
        funcMaria.put("email", "maria.santos@tangaradaserra.mt.gov.br");
        funcMaria.put("tipoUsuario", "funcionario");
        funcMaria.put("dataCadastro", Timestamp.now());
        batch.set(db.collection("usuarios").document(USER_ID_FUNCIONARIO_MARIA), funcMaria);

        Map<String, Object> funcPedro = new HashMap<>();
        funcPedro.put("nomeCompleto", "Pedro Rocha (Atendimento Saúde)");
        funcPedro.put("email", "pedro.rocha@tangaradaserra.mt.gov.br");
        funcPedro.put("tipoUsuario", "funcionario");
        funcPedro.put("dataCadastro", Timestamp.now());
        batch.set(db.collection("usuarios").document(USER_ID_FUNCIONARIO_PEDRO), funcPedro);

        Map<String, Object> gestorSofia = new HashMap<>(); // Renomeado para gestor
        gestorSofia.put("nomeCompleto", "Sofia Oliveira (Gestora Geral)");
        gestorSofia.put("email", "sofia.oliveira@tangaradaserra.mt.gov.br");
        gestorSofia.put("tipoUsuario", "gestor"); // Tipo de usuário para gestor
        gestorSofia.put("dataCadastro", Timestamp.now());
        batch.set(db.collection("usuarios").document(USER_ID_GESTOR_SOFIA), gestorSofia);


        // --- Seed Ocorrências ---
        try {
            String[] bairrosTangara = {"Centro", "Vila Alta", "Jardim Europa", "Parque Industrial", "Jardim Califórnia", "Progresso", "Vila Esmeralda", "Jardim dos Ipês", "Jardim Itália", "Santa Lúcia", "Vila Goiânia", "Jardim Tarumã", "Jardim San Diego", "Jardim Shangri-lá", "Jardim do Lago", "Vila Nazaré", "Jardim Mirassol", "Jardim Acapulco", "Alto da Boa Vista", "Distrito de Progresso"};
            String[] categorias = {"Buraco na Via", "Lâmpada Queimada/Defeito na Iluminação", "Lixo Acumulado/Problema na Coleta", "Vazamento de Água ou Esgoto", "Semáforo com Defeito/Problema de Sinalização", "Calçada Irregular ou Obstruída", "Problema em Ponto de Ônibus", "Árvore Precisando de Poda/Remoção", "Animal Abandonado/Perdido", "Terreno Baldio com Mato Alto", "Poluição Sonora", "Falta de Acessibilidade"};
            String[] statusOcorrencia = {"Recebida", "Em Análise", "Em Andamento", "Resolvida", "Rejeitada"};
            String[] userIdsCidadaosAll = {USER_ID_ANA, USER_ID_BRUNO, USER_ID_CARLA, USER_ID_DANIEL, USER_ID_ELISA};
            String[] userIdsFuncionariosAll = {USER_ID_FUNCIONARIO_JOAO, USER_ID_FUNCIONARIO_MARIA, USER_ID_FUNCIONARIO_PEDRO, USER_ID_GESTOR_SOFIA};

            double baseLat = -14.6195;
            double baseLng = -57.4895;
            Random random = new Random();

            String[] titulosExemplo = {
                    "Cratera perigosa na via", "Poste piscando sem parar", "Lixão a céu aberto na esquina", "Esgoto com mau cheiro vazando", "Sinal de trânsito inoperante",
                    "Calçada totalmente esburacada", "Ponto de ônibus depredado e sem abrigo", "Árvore antiga ameaçando cair", "Grupo de cães abandonados", "Terreno baldio necessitando limpeza urgente",
                    "Som alto durante a madrugada", "Rampa de acesso quebrada no posto"
            };
            String[] descricoesExemplo = {
                    "Existe um buraco muito grande que já causou problemas para veículos. Risco de acidentes graves, especialmente à noite na rua principal.",
                    "O poste da rua X está piscando constantemente há vários dias, causando incômodo e dificultando a visibilidade noturna.",
                    "Moradores estão descartando lixo irregularmente neste local, formando um pequeno lixão, atraindo vetores e com forte odor.",
                    "Vazamento de esgoto com odor forte está escorrendo pela rua e entrando em algumas garagens de residências próximas.",
                    "O semáforo do cruzamento da Avenida Y com a Rua Z está desligado há mais de 24 horas, perigo iminente de colisões.",
                    "A calçada está completamente destruída em frente ao mercado, impossibilitando a passagem de pedestres, especialmente idosos e cadeirantes.",
                    "O ponto de ônibus da linha Circular Bairro foi vandalizado, está sem assentos e sem cobertura para chuva/sol, prejudicando os usuários.",
                    "Uma árvore de grande porte na praça central está com galhos muito secos e inclinada, parecendo que vai cair sobre a rede elétrica ou área de passagem.",
                    "Encontramos um grupo de aproximadamente 5 cães aparentemente abandonados e com fome vagando pela região do bairro Jardim Califórnia.",
                    "O terreno baldio ao lado da escola municipal está com mato muito alto, gerando preocupação com segurança e proliferação de mosquitos.",
                    "Barulho excessivo de música alta vindo de uma festa no estabelecimento X todos os finais de semana até tarde da noite.",
                    "A rampa de acesso para cadeirantes na entrada principal do posto de saúde central está quebrada e com vergalhões expostos."
            };
            String[] resolucoesExemplo = {
                    "Equipe de obras realizou o recapeamento asfáltico no local indicado.", "Lâmpada do poste substituída e sistema elétrico verificado pela equipe de iluminação pública.", "Limpeza do local realizada e notificação enviada ao proprietário do terreno para cercamento.",
                    "Vazamento de esgoto contido e reparo na rede de tubulação efetuado pela companhia de saneamento.", "Equipe de trânsito consertou o semáforo e normalizou a sinalização.", "Calçada refeita conforme as normas de acessibilidade vigentes.",
                    "Ponto de ônibus reparado pela secretaria de obras e nova cobertura instalada.", "Árvore podada preventivamente pela equipe de parques e jardins do município.", "Animais recolhidos pela equipe do centro de zoonoses para cuidados e adoção.",
                    "Proprietário do terreno foi notificado para realizar a limpeza e capina.", "Fiscalização de posturas orientou o estabelecimento sobre os limites de ruído permitidos por lei.", "Rampa de acesso do posto de saúde foi reparada e sinalizada."
            };

            for (int i = 1; i <= 50; i++) { // Gerar 50 ocorrências
                Ocorrencia oc = new Ocorrencia();
                oc.setUserId(userIdsCidadaosAll[random.nextInt(userIdsCidadaosAll.length)]);
                oc.setCategoria(categorias[random.nextInt(categorias.length)]);
                String statusAtual = statusOcorrencia[random.nextInt(statusOcorrencia.length)];
                oc.setStatus(statusAtual);
                oc.setBairro(bairrosTangara[random.nextInt(bairrosTangara.length)]);

                double latOffset = (Math.random() - 0.5) * 0.12; // Variação maior para espalhar mais
                double lngOffset = (Math.random() - 0.5) * 0.12;
                oc.setLocalizacao(new GeoPoint(baseLat + latOffset, baseLng + lngOffset));

                int exemploIndex = random.nextInt(titulosExemplo.length);
                oc.setTitulo(titulosExemplo[exemploIndex] + " (" + oc.getBairro() + ") #" + i);
                oc.setDescricao(descricoesExemplo[exemploIndex] + " Detalhe adicional para a ocorrência #" + i + ".");
                oc.setEnderecoTexto("Rua Fictícia " + (100 + i*2) + ", Bairro " + oc.getBairro() + ", Tangará da Serra - MT");

                // Para datas, @ServerTimestamp cuidará disso.
                // Se precisar de datas passadas específicas para teste, você pode definir manualmente:
                // long diasAtras = random.nextInt(90) + 1; // De 1 a 90 dias atrás
                // oc.setDataRegistro(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(diasAtras)));
                // if (!statusAtual.equals("Recebida")) {
                //    long diasAtualizacao = random.nextInt((int)diasAtras) + 1; // Atualização não pode ser antes do registro
                //    oc.setDataUltimaAtualizacao(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(Math.min(diasAtras -1, diasAtualizacao )) ) );
                // } else {
                //    oc.setDataUltimaAtualizacao(oc.getDataRegistro());
                // }


                if (random.nextDouble() > 0.4) { // 60% chance de ter mídia
                    List<String> midias = new ArrayList<>();
                    midias.add("https://via.placeholder.com/400x300/" + String.format("%06x", random.nextInt(0xffffff + 1)) + "/FFFFFF?Text=FotoOcorrencia" + i + "_A.jpg");
                    if (random.nextDouble() > 0.7) { // Chance de ter uma segunda mídia
                        midias.add("https://via.placeholder.com/400x300/" + String.format("%06x", random.nextInt(0xffffff + 1)) + "/FFFFFF?Text=FotoOcorrencia" + i + "_B.jpg");
                    }
                    oc.setMediaUrls(midias);
                }

                if (!statusAtual.equals("Recebida") && !statusAtual.equals("Em Análise")) {
                    oc.setAtribuidoPara(userIdsFuncionariosAll[random.nextInt(userIdsFuncionariosAll.length)]);
                    if (statusAtual.equals("Resolvida") || statusAtual.equals("Rejeitada")) {
                        oc.setResolucaoFuncionario(statusAtual.equals("Resolvida") ? resolucoesExemplo[random.nextInt(resolucoesExemplo.length)] : "Ocorrência avaliada e considerada improcedente ou fora da alçada municipal.");
                    }
                }
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

            Indicator ind2 = new Indicator("leitos_uti_disponiveis_jun25", "Leitos de UTI Disponíveis", "12", "Secretaria Municipal de Saúde", "Junho/2025", "Todos", "Saúde");
            batch.set(db.collection("indicadores").document(ind2.getId()), ind2);

            Indicator ind3 = new Indicator("km_asfalto_novo_2024", "Km de Asfalto Novo", "15 km", "Secretaria de Obras", "2024", "Todos", "Infraestrutura");
            ind3.setChartType("LINHA");
            batch.set(db.collection("indicadores").document(ind3.getId()), ind3);

            Indicator ind4 = new Indicator("indice_coleta_seletiva_centro", "Índice de Coleta Seletiva", "65%", "Serviços Urbanos", "Maio/2025", "Centro", "Meio Ambiente");
            ind4.setChartType("PIZZA");
            batch.set(db.collection("indicadores").document(ind4.getId()), ind4);

            Indicator ind5 = new Indicator("ocorrencias_furto_vilaalta_2024", "Ocorrências de Furto (Vila Alta)", "8", "Polícia Militar", "2024 (Acumulado Ano)", "Vila Alta", "Segurança");
            batch.set(db.collection("indicadores").document(ind5.getId()), ind5);

            Indicator ind6 = new Indicator("tempo_medio_atendimento_obras", "Tempo Médio Resposta Obras", "7 dias", "Secretaria de Obras", "Trimestre Atual", "Todos", "Gestão");
            ind6.setChartType("GAUGE"); // Tipo de gráfico de velocímetro/medidor
            batch.set(db.collection("indicadores").document(ind6.getId()), ind6);

            Indicator ind7 = new Indicator("arborizacao_urbana_jdEuropa", "Índice de Arborização (Jd. Europa)", "85%", "Secretaria Meio Ambiente", "2024", "Jardim Europa", "Meio Ambiente");
            batch.set(db.collection("indicadores").document(ind7.getId()), ind7);


        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar indicadores de exemplo", e);
        }

        batch.commit()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Banco de dados Firestore populado com dados expandidos!"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao popular o banco de dados Firestore com dados expandidos.", e));
    }
}
