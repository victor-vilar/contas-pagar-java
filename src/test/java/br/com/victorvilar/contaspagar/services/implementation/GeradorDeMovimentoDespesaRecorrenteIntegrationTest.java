package br.com.victorvilar.contaspagar.services.implementation;

import br.com.victorvilar.contaspagar.repositories.MovimentoPagamentoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

//Spring boot test falo para iniciar o contexto do spring no teste
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class GeradorDeMovimentoDespesaRecorrenteIntegrationTest {

    @Autowired
    private GeradorDeMovimentoDespesaRecorrente gerador;

    @Autowired
    private MovimentoPagamentoRepository repository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("pgvector/pgvector:pg16")
            .withDatabaseName("teste")
            .withUsername("teste")
            .withPassword("teste");


    //passa configurações dinamicas para o spring ao invez dele puxar as configurações do application.properties
    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",postgres::getJdbcUrl);
        registry.add("spring.datasource.useraname",postgres::getUsername);
        registry.add("spring.datasource.password",postgres::getPassword);
    }













}
