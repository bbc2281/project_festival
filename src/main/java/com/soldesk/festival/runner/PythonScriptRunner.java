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
        ClassPathResource resource_f = new ClassPathResource("static/py/festivalApi.py");
        ClassPathResource resource_s = new ClassPathResource("static/py/search.py");
        
        Path path1 = Paths.get(resource_f.getURI());
        Path path2 = Paths.get(resource_s.getURI());

        ProcessBuilder pb1 = new ProcessBuilder(pythonPath, path1.toString());
        pb1.inheritIO();
        pb1.start();

        ProcessBuilder pb2 = new ProcessBuilder(pythonPath, path2.toString());
        pb2.inheritIO();
        pb2.start();
    }
}
