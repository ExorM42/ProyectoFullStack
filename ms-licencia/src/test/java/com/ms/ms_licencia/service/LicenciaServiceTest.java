package com.ms.ms_licencia.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ms.ms_licencia.client.CitaClient;
import com.ms.ms_licencia.client.DoctorClient;
import com.ms.ms_licencia.client.PacienteClient;
import com.ms.ms_licencia.dto.PacienteDTO;
import com.ms.ms_licencia.repository.LicenciaMedicaRepository;
import com.ms.ms_licencia.repository.TipoLicenciaRepository;

@ExtendWith(MockitoExtension.class)
public class LicenciaServiceTest {

    @Mock
    private LicenciaMedicaRepository licenciaRepo;

    @Mock
    private TipoLicenciaRepository tipoLicenciaRepo;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private DoctorClient doctorClient;

    @Mock
    private CitaClient citaClient;

    @InjectMocks
    private LicenciaService licenciaService;


    @Test
    void givenIdPaciente_whenFindById_thenReturnPaciente(){

        //GIVEN
        Long PacienteId = 1L;

        PacienteDTO paciente = new PacienteDTO();
        

    }
    

}
