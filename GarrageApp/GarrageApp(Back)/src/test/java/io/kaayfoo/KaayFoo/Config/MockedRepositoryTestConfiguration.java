package io.kaayfoo.KaayFoo.Config;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.kaayfoo.KaayFoo.Repository.GarrageRepository;

@Configuration
public class MockedRepositoryTestConfiguration {

  @Bean
  @Primary
  public GarrageRepository mockedOperatorRepository() {
    return mock(GarrageRepository.class);
  }

  @Bean
  public GarrageService GarrageService(GarrageRepository repo, GarrageMapper mapper, GarrageServiceImpl operatorRepository) {
    return new OperatorServiceImpl(repo, mapper, operatorRepository);
  }
}
