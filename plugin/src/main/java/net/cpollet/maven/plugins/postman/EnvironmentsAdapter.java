package net.cpollet.maven.plugins.postman;

import lombok.AllArgsConstructor;
import net.cpollet.maven.plugins.postman.frontend.api.Context;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class EnvironmentsAdapter {
    private final List<Environment> environments;

    public Map<String, Context> contexts() throws DuplicateEnvironmentNameException {
        List<String> duplicates = findDuplicates(extractNames());

        if (!duplicates.isEmpty()) {
            throw new DuplicateEnvironmentNameException(duplicates);
        }

        return environments.stream()
                .map(EnvironmentAdapter::new)
                .collect(Collectors.toMap(
                        EnvironmentAdapter::getName,
                        e -> Context.builder()
                                .baseUrl(e.getBaseUrl())
                                .username(e.getUsername())
                                .password(e.getPassword())
                                .build()
                ));
    }

    private List<String> findDuplicates(List<String> names) {
        return names.stream()
                .distinct()
                .filter(e -> Collections.frequency(names, e) > 1)
                .collect(Collectors.toList());
    }

    private List<String> extractNames() {
        return environments.stream()
                .map(Environment::getName)
                .collect(Collectors.toList());
    }
}
