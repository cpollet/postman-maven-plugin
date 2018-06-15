package net.cpollet.maven.plugins.postman;

import org.apache.maven.plugin.testing.MojoRule;

import org.junit.Ignore;
import org.junit.Rule;

import org.junit.Test;

import java.io.File;

@Ignore
public class GenerateMojoTest {
    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable {
        }

        @Override
        protected void after() {
        }
    };

    @Test
    public void generate_needsEitherBaseUrlOrEnvironment() throws Exception {
        File pom = new File("target/test-classes/test");

        GenerateMojo generateMojo = (GenerateMojo) rule.lookupConfiguredMojo(pom, "generateMojo");

        generateMojo.execute();
    }
}

