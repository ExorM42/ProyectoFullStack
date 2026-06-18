package com.ms.ms_auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ms.ms_auth.dto.AuthResponseDTO;
import com.ms.ms_auth.dto.LoginRequestDTO;
import com.ms.ms_auth.dto.RegisterRequestDTO;
import com.ms.ms_auth.model.Rol;
import com.ms.ms_auth.model.Usuario;
import com.ms.ms_auth.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO registrar(RegisterRequestDTO dto){
        if (usuarioRepository.existsByEmail(dto.getEmail())){
            throw new IllegalStateException("Ya existe un usuario con ese email");
        }

        if (dto.getRol() == Rol.PACIENTE && dto.getPacienteId() == null){
            throw new IllegalStateException("Un usuario de tipo PACIENTE debe tener ID");
        }

        if (dto.getRol() == Rol.DOCTOR && dto.getDoctorId() == null){
            throw new IllegalStateException("Un usuario de tipo DOCTOR debe tener un ID");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(dto.getRol());
        usuario.setPacienteId(dto.getPacienteId());
        usuario.setDoctorId(dto.getDoctorId());
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);
        String token = jwtService.generarToken(guardado);

        return new AuthResponseDTO(token, guardado.getRol(), guardado.getPacienteId(), guardado.getDoctorId());
    }

    public AuthResponseDTO login(LoginRequestDTO dto){
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalStateException("Credenciales inválidas"));
        
        if (!usuario.getActivo()){
            throw new IllegalStateException("Usuario inactivo");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())){
            throw new IllegalStateException("Credenciales inválidas");
        }

        String token = jwtService.generarToken(usuario);

        return new AuthResponseDTO(token, usuario.getRol(), usuario.getPacienteId(), usuario.getDoctorId());
    }

}
