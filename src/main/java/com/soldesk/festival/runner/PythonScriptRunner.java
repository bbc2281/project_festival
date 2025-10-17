package com.soldesk.festival.runner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PythonScriptRunner implements ApplicationRunner{

    @Override
    public void run(ApplicationArguments args) throws Exception {
        runPythonScript();
    }
    
    public void runPythonScript() throws IOException{
        String pythonPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\python.exe";
        ClassPathResource resource = new ClassPathResource("static/py/festivalApi.py");
        
        Path path = Paths.get(resource.getURI());
        String pyAbsolutePath = path.toString();

        ProcessBuilder pb = new ProcessBuilder(pythonPath, pyAbsolutePath);
        pb.inheritIO();
        pb.start();
    }
}
