package source.testmodule.Infrastructure.Configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "source.testmodule.Infrastructure.Persitence.RepositoryAdapters")
public class JpaConfig {


}
