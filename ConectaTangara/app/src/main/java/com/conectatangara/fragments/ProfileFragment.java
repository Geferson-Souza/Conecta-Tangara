package com.conectatangara.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

// import com.bumptech.glide.Glide; // Para carregar o avatar do usuário
import com.conectatangara.R;
import com.conectatangara.activities.LoginActivity;
import com.conectatangara.activities.MainActivity; // Para setar o título da Toolbar
import com.google.android.material.materialswitch.MaterialSwitch; // Import para MaterialSwitch
// import com.google.firebase.auth.FirebaseAuth; // Para logout e dados do usuário
// import com.google.firebase.auth.FirebaseUser; // Para dados do usuário

public class ProfileFragment extends Fragment {

    private ImageView ivProfileAvatar;
    private TextView tvProfileUserName;
    private TextView tvProfileUserEmail;
    private Button buttonEditProfile; // Mudado para Button, conforme layout
    private TextView tvChangePassword;
    private TextView tvMyOccurrencesShortcut;
    private MaterialSwitch switchNotificationsGeneral;
    private MaterialSwitch switchNotificationsStatus;
    private TextView tvAboutApp;
    private TextView tvTermsOfUse;
    private TextView tvPrivacyPolicy;
    private Button buttonLogout;

    // private FirebaseAuth mAuth; // Descomente quando for integrar com Firebase Auth

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout para este fragmento
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // mAuth = FirebaseAuth.getInstance(); // Descomente para Firebase Auth

        // Inicializa as Views
        ivProfileAvatar = view.findViewById(R.id.iv_profile_avatar);
        tvProfileUserName = view.findViewById(R.id.tv_profile_user_name);
        tvProfileUserEmail = view.findViewById(R.id.tv_profile_user_email);
        buttonEditProfile = view.findViewById(R.id.button_edit_profile); // ID do MaterialButton
        tvChangePassword = view.findViewById(R.id.tv_change_password);
        tvMyOccurrencesShortcut = view.findViewById(R.id.tv_my_occurrences_shortcut);
        switchNotificationsGeneral = view.findViewById(R.id.switch_notifications_general);
        switchNotificationsStatus = view.findViewById(R.id.switch_notifications_status);
        tvAboutApp = view.findViewById(R.id.tv_about_app);
        tvTermsOfUse = view.findViewById(R.id.tv_terms_of_use);
        tvPrivacyPolicy = view.findViewById(R.id.tv_privacy_policy);
        buttonLogout = view.findViewById(R.id.button_logout);

        loadUserProfileData(); // Carrega dados (placeholders por enquanto)
        setupClickListeners(); // Configura os cliques
    }

    @Override
    public void onResume() {
        super.onResume();
        // Define o título na Toolbar da MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_profile));
        }
    }

    private void loadUserProfileData() {
        // FirebaseUser currentUser = mAuth.getCurrentUser(); // Descomente para Firebase
        // if (currentUser != null && getContext() != null) {
        //    tvProfileUserName.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : getString(R.string.profile_user_name_placeholder));
        //    tvProfileUserEmail.setText(currentUser.getEmail());
        //    if (currentUser.getPhotoUrl() != null) {
        //        Glide.with(getContext())
        //                .load(currentUser.getPhotoUrl())
        //                .placeholder(R.drawable.ic_profile_avatar_placeholder) // Seu placeholder
        //                .error(R.drawable.ic_profile_avatar_placeholder)       // Ícone de erro
        //                .circleCrop() // Para deixar a imagem redonda
        //                .into(ivProfileAvatar);
        //    } else {
        //        ivProfileAvatar.setImageResource(R.drawable.ic_profile_avatar_placeholder);
        //    }
        // } else {
        // Dados de placeholder se não houver usuário ou para teste de layout
        tvProfileUserName.setText(getString(R.string.profile_user_name_placeholder));
        tvProfileUserEmail.setText(getString(R.string.profile_user_email_placeholder));
        if (getContext() != null) { // Para carregar o drawable placeholder
            ivProfileAvatar.setImageResource(R.drawable.ic_profile_avatar_placeholder);
        }
        // }
    }

    private void setupClickListeners() {
        buttonEditProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Editar Perfil - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para uma Activity/Fragment de edição de perfil
        });

        tvChangePassword.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Alterar Senha - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Mostrar diálogo ou navegar para tela de alteração de senha
        });

        tvMyOccurrencesShortcut.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Minhas Ocorrências - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para o MyOccurrencesFragment.
            // Exemplo: if (getActivity() instanceof MainActivity) {
            //             ((MainActivity) getActivity()).navigateToFragment(new MyOccurrencesFragment(), "MY_OCCURRENCES_TAG");
            //          }
            // Ou se estiver usando BottomNavigationView, selecionar o item correspondente:
            // if (getActivity() instanceof MainActivity) {
            //    BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation_view);
            //    if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_my_occurrences);
            // }
        });

        switchNotificationsGeneral.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), getString(R.string.profile_setting_notifications_general) + (isChecked ? ": Ativadas" : ": Desativadas"), Toast.LENGTH_SHORT).show();
            // TODO: Salvar esta preferência do usuário (ex: SharedPreferences)
        });

        switchNotificationsStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), getString(R.string.profile_setting_notifications_status_updates) + (isChecked ? ": Ativadas" : ": Desativadas"), Toast.LENGTH_SHORT).show();
            // TODO: Salvar esta preferência do usuário
        });

        tvAboutApp.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Sobre o App - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Mostrar um diálogo "Sobre" ou abrir uma tela de "Sobre"
        });

        tvTermsOfUse.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Termos de Uso - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Abrir link para os Termos de Uso (ex: com Intent para URL)
        });

        tvPrivacyPolicy.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Política de Privacidade - Funcionalidade a implementar", Toast.LENGTH_SHORT).show();
            // TODO: Abrir link para a Política de Privacidade
        });

        buttonLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void showLogoutConfirmationDialog() {
        if (getContext() == null) return; // Evita crash se o fragmento for desanexado
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.profile_confirm_logout_title))
                .setMessage(getString(R.string.profile_confirm_logout_message))
                .setPositiveButton(getString(R.string.dialog_yes), (dialog, which) -> logoutUser())
                .setNegativeButton(getString(R.string.dialog_no), null)
                .setIcon(R.drawable.ic_logout) // Opcional: adiciona um ícone ao diálogo
                .show();
    }

    private void logoutUser() {
        // Descomente e use quando o Firebase Auth estiver configurado
        // FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // if (mAuth != null) {
        //    mAuth.signOut();
        // }

        // TODO: Limpar quaisquer outros dados de sessão local se necessário

        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finishAffinity(); // Fecha todas as activities da tarefa atual
        }
    }
}
    