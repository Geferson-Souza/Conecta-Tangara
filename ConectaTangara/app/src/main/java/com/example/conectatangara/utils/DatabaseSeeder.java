package com.example.conectatangara.utils;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DatabaseSeeder {

    private static final String TAG = "DatabaseSeeder";

    public static void seedDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Log.e(TAG, "Nenhum usuário autenticado. Faça login antes de rodar o seeder.");
            return;
        }

        String uid = currentUser.getUid();
        Log.d(TAG, "Usando UID autenticado: " + uid);

        seedUsers(db, uid);
        seedOccurrenceCategories(db);
        seedOccurrences(db, uid);
        seedOccurrenceUpdatesHistory(db, uid);
        seedOccurrenceComments(db, uid);
        seedOccurrenceSupports(db, uid);
        seedEvents(db, uid);
        seedPublicStatsIndicators(db);
    }

    private static void seedUsers(FirebaseFirestore db, String uid) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Geferson Almeida");
        user.put("email", "geferson@email.com");
        user.put("userType", "cidadao");
        user.put("registrationDate", Timestamp.now());
        user.put("profileImageUrl", "");
        user.put("fcmToken", "abc123");
        user.put("isBlocked", false);
        user.put("lastLogin", Timestamp.now());

        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(unused -> Log.d(TAG, "Usuário criado"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao criar usuário", e));
    }

    private static void seedOccurrenceCategories(FirebaseFirestore db) {
        Map<String, Object> category = new HashMap<>();
        category.put("name", "Buracos em Vias");
        category.put("description", "Ruas danificadas com buracos");
        category.put("iconName", "ic_buraco");
        category.put("isActive", true);
        category.put("order", 1);

        db.collection("occurrence_categories")
                .add(category)
                .addOnSuccessListener(ref -> Log.d(TAG, "Categoria adicionada: " + ref.getId()))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao adicionar categoria", e));
    }

    private static void seedOccurrences(FirebaseFirestore db, String uid) {
        Map<String, Object> occurrence = new HashMap<>();
        occurrence.put("userId", uid);
        occurrence.put("userNameDisplay", "Geferson Almeida");
        occurrence.put("userProfileImageUrlDisplay", "");
        occurrence.put("categoryId", "cat_001"); // ou substitua por ID dinâmico se necessário
        occurrence.put("categoryName", "Buracos em Vias");
        occurrence.put("description", "Buraco enorme na rua central");
        occurrence.put("latitude", -14.074);
        occurrence.put("longitude", -57.064);
        occurrence.put("addressText", "Rua Central, Tangará da Serra");
        occurrence.put("photoUrls", Arrays.asList("https://example.com/foto1.jpg"));
        occurrence.put("videoUrls", Arrays.asList());
        occurrence.put("creationTimestamp", Timestamp.now());
        occurrence.put("lastUpdateTimestamp", Timestamp.now());
        occurrence.put("status", "enviada");
        occurrence.put("isAnonymous", false);
        occurrence.put("supportCount", 0);
        occurrence.put("commentCount", 0);
        occurrence.put("resolutionNotes", "");
        occurrence.put("assignedDepartment", "");

        db.collection("occurrences")
                .add(occurrence)
                .addOnSuccessListener(ref -> Log.d(TAG, "Ocorrência adicionada: " + ref.getId()))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao adicionar ocorrência", e));
    }

    private static void seedOccurrenceUpdatesHistory(FirebaseFirestore db, String uid) {
        Map<String, Object> update = new HashMap<>();
        update.put("occurrenceId", "oc_001");
        update.put("adminUserId", uid);
        update.put("adminUserName", "Geferson Almeida");
        update.put("timestamp", Timestamp.now());
        update.put("previousStatus", "enviada");
        update.put("newStatus", "em_analise");
        update.put("internalNote", "Verificar urgência");
        update.put("publicCommentToCitizen", "Estamos analisando sua solicitação.");

        db.collection("occurrence_updates_history")
                .add(update)
                .addOnSuccessListener(ref -> Log.d(TAG, "Histórico criado"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao criar histórico", e));
    }

    private static void seedOccurrenceComments(FirebaseFirestore db, String uid) {
        Map<String, Object> comment = new HashMap<>();
        comment.put("occurrenceId", "oc_001");
        comment.put("userId", uid);
        comment.put("userNameDisplay", "Geferson Almeida");
        comment.put("userProfileImageUrlDisplay", "");
        comment.put("text", "Essa rua está intransitável!");
        comment.put("timestamp", Timestamp.now());
        comment.put("isModerated", false);
        comment.put("isHiddenByAdmin", false);

        db.collection("occurrence_comments")
                .add(comment)
                .addOnSuccessListener(ref -> Log.d(TAG, "Comentário adicionado"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao adicionar comentário", e));
    }

    private static void seedOccurrenceSupports(FirebaseFirestore db, String uid) {
        Map<String, Object> support = new HashMap<>();
        support.put("userId", uid);
        support.put("occurrenceId", "oc_001");
        support.put("timestamp", Timestamp.now());

        db.collection("occurrence_supports")
                .document(uid + "_oc_001")
                .set(support)
                .addOnSuccessListener(unused -> Log.d(TAG, "Apoio registrado"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao registrar apoio", e));
    }

    private static void seedEvents(FirebaseFirestore db, String uid) {
        Map<String, Object> event = new HashMap<>();
        event.put("title", "Feira Cultural");
        event.put("description", "Evento comunitário com música e comida");
        event.put("startTimestamp", Timestamp.now());
        event.put("locationName", "Praça Central");
        event.put("category", "Cultura");
        event.put("isOfficial", true);
        event.put("addedByAdminId", uid);
        event.put("creationTimestamp", Timestamp.now());

        db.collection("events")
                .add(event)
                .addOnSuccessListener(ref -> Log.d(TAG, "Evento adicionado"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao adicionar evento", e));
    }

    private static void seedPublicStatsIndicators(FirebaseFirestore db) {
        Map<String, Object> indicator = new HashMap<>();
        indicator.put("indicatorName", "População Estimada");
        indicator.put("value", "105000");
        indicator.put("unit", "habitantes");
        indicator.put("referencePeriod", "2023");
        indicator.put("sourceName", "IBGE Cidades");
        indicator.put("dataCategory", "Demografia");
        indicator.put("municipalityCode", "5107958");
        indicator.put("lastFetchedTimestamp", Timestamp.now());
        indicator.put("detailsUrl", "");
        indicator.put("notes", "");

        db.collection("public_stats_indicators")
                .document("populacao_2023")
                .set(indicator)
                .addOnSuccessListener(unused -> Log.d(TAG, "Indicador adicionado"))
                .addOnFailureListener(e -> Log.e(TAG, "Erro ao adicionar indicador", e));
    }
}
