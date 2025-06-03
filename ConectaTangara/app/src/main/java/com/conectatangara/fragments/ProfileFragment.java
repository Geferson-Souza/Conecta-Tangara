package com.conectatangara.fragments;

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

import com.bumptech.glide.Glide;
import com.conectatangara.R;
import com.conectatangara.activities.LoginActivity;
import com.conectatangara.activities.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private ImageView ivProfileAvatar;
    private TextView tvProfileUserName;
    private TextView tvProfileUserEmail;
    private Button buttonEditProfile;
    private TextView tvChangePassword;
    private TextView tvMyOccurrencesShortcut;
    private MaterialSwitch switchNotificationsGeneral;
    private MaterialSwitch switchNotificationsStatus;
    private TextView tvAboutApp;
    private TextView tvTermsOfUse;
    private TextView tvPrivacyPolicy;
    private Button buttonLogout;

    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance(); // Inicializa o FirebaseAuth
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfileAvatar = view.findViewById(R.id.iv_profile_avatar);
        tvProfileUserName = view.findViewById(R.id.tv_profile_user_name);
        tvProfileUserEmail = view.findViewById(R.id.tv_profile_user_email);
        buttonEditProfile = view.findViewById(R.id.button_edit_profile);
        tvChangePassword = view.findViewById(R.id.tv_change_password);
        tvMyOccurrencesShortcut = view.findViewById(R.id.tv_my_occurrences_shortcut);
        switchNotificationsGeneral = view.findViewById(R.id.switch_notifications_general);
        switchNotificationsStatus = view.findViewById(R.id.switch_notifications_status);
        tvAboutApp = view.findViewById(R.id.tv_about_app);
        tvTermsOfUse = view.findViewById(R.id.tv_terms_of_use);
        tvPrivacyPolicy = view.findViewById(R.id.tv_privacy_policy);
        buttonLogout = view.findViewById(R.id.button_logout);

        loadUserProfileData();
        setupClickListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.toolbar_title_profile));
        }
        // Recarregar dados do usuário caso haja alguma mudança (ex: nome ou foto atualizada em outra tela)
        // Isso é útil se o usuário editar o perfil e voltar para esta tela.
        loadUserProfileData();
    }

    private void loadUserProfileData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && getContext() != null) {
            // Define o nome do usuário
            if (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
                tvProfileUserName.setText(currentUser.getDisplayName());
            } else if (currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
                // Se não houver nome de exibição, tenta usar o email antes do placeholder
                tvProfileUserName.setText(currentUser.getEmail());
            } else {
                tvProfileUserName.setText(getString(R.string.profile_user_name_placeholder));
            }

            // Define o email do usuário
            tvProfileUserEmail.setText(currentUser.getEmail());

            // Carrega a foto do perfil usando Glide
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(getContext().getApplicationContext()) // Usar applicationContext com Glide em Fragments
                        .load(currentUser.getPhotoUrl())
                        .placeholder(R.drawable.ic_profile_avatar_placeholder) // Certifique-se que este drawable existe
                        .error(R.drawable.ic_profile_avatar_placeholder)       // Ícone em caso de erro
                        .circleCrop() // Para deixar a imagem redonda
                        .into(ivProfileAvatar);
            } else {
                ivProfileAvatar.setImageResource(R.drawable.ic_profile_avatar_placeholder); // Placeholder padrão
            }
        } else {
            // Dados de placeholder se não houver usuário logado ou contexto nulo
            tvProfileUserName.setText(getString(R.string.profile_user_name_placeholder));
            tvProfileUserEmail.setText(getString(R.string.profile_user_email_placeholder));
            if (getContext() != null) {
                ivProfileAvatar.setImageResource(R.drawable.ic_profile_avatar_placeholder);
            }
        }
    }

    private void setupClickListeners() {
        buttonEditProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Editar Perfil - A implementar", Toast.LENGTH_SHORT).show();
            // TODO: Navegar para uma Activity/Fragment de edição de perfil
        });

        tvChangePassword.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Alterar Senha - A implementar", Toast.LENGTH_SHORT).show();
            // TODO: Mostrar diálogo ou navegar para tela de alteração de senha
        });

        tvMyOccurrencesShortcut.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation_view);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_my_occurrences); // Certifique-se que este ID existe no menu
                }
            }
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
            // TODO: Mostrar um diálogo "Sobre" ou abrir uma tela de "Sobre"
            Toast.makeText(getContext(), "Sobre o App - A implementar", Toast.LENGTH_SHORT).show();
        });

        tvTermsOfUse.setOnClickListener(v -> {
            // TODO: Abrir link para os Termos de Uso (ex: com Intent para URL)
            Toast.makeText(getContext(), "Termos de Uso - A implementar", Toast.LENGTH_SHORT).show();
        });

        tvPrivacyPolicy.setOnClickListener(v -> {
            // TODO: Abrir link para a Política de Privacidade
            Toast.makeText(getContext(), "Política de Privacidade - A implementar", Toast.LENGTH_SHORT).show();
        });

        buttonLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void showLogoutConfirmationDialog() {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.profile_confirm_logout_title))
                .setMessage(getString(R.string.profile_confirm_logout_message))
                .setPositiveButton(getString(R.string.dialog_yes), (dialog, which) -> logoutUser())
                .setNegativeButton(getString(R.string.dialog_no), null)
                .setIcon(R.drawable.ic_logout) // Certifique-se que este drawable existe
                .show();
    }

    private void logoutUser() {
        if (mAuth != null) {
            mAuth.signOut();
        }
        // TODO: Limpar quaisquer outros dados de sessão local se necessário (ex: SharedPreferences de preferências de notificação)

        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finishAffinity(); // Fecha todas as activities da tarefa atual
        }
    }
}
