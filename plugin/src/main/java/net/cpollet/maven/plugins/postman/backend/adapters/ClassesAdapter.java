package net.cpollet.maven.plugins.postman.backend.adapters;

import lombok.AllArgsConstructor;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ClassesAdapter {
    private final List<Class> classes;

    public List<Endpoint> getEndpoints() {
        return classes.stream()
                .map(c -> new ClassAdapter(c).getEndpoints())
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
