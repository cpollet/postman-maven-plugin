package net.cpollet.maven.plugins.postman;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;

import org.junit.Ignore;
import org.junit.Rule;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;

@Ignore
public class GenerateTest {
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
    public void testSomething() throws Exception {
        File pom = new File("target/test-classes/project-to-test");

        Generate myMojo = (Generate) rule.lookupConfiguredMojo(pom, "package");
        assertNotNull(myMojo);
        myMojo.execute();

    }
}

