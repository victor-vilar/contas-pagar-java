package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.repositories.DespesaRepository;
import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class LancamentoMovimentoDespesaRecorrenteIntegrationTest {

    @Autowired
    private GeradorDeMovimentoDespesaRecorrente gerador;

    @Container
    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("pgvector/pgvector:pg16");
            

    @BeforeEach
    public void setUp(){
        postgres.start();
    }

    @AfterEach
    public void endUp(){
        postgres.stop();
    }











}
