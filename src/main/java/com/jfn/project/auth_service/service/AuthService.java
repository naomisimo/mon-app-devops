package com.jfn.project.auth_service.service;

import com.jfn.project.auth_service.beans.Utilisateur;
import com.jfn.project.auth_service.dto.LoginRequest;
import com.jfn.project.auth_service.dto.LoginResponse;
import com.jfn.project.auth_service.repository.UtilisateurRepository;
import com.jfn.project.auth_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Utilisateur register(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.setDateCreation(LocalDateTime.now());
        return utilisateurRepository.save(utilisateur);
    }

    public LoginResponse login(LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        if (!utilisateur.getActif()) {
            throw new RuntimeException("Compte désactivé");
        }

        utilisateur.setDerniereConnexion(LocalDateTime.now());
        utilisateurRepository.save(utilisateur);

        String token = jwtService.generateToken(utilisateur.getEmail(), utilisateur.getRole());
        return new LoginResponse(token, utilisateur.getEmail(), utilisateur.getRole());
    }
}