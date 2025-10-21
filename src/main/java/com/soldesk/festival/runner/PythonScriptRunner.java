package com.soldesk.festival.runner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PythonScriptRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        runPythonScript();
        runFastApiServer();
    }

    public void runPythonScript() throws IOException {
        String pythonPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\python.exe";
        ClassPathResource resource = new ClassPathResource("static/py/festivalApi.py");
        Path path = Paths.get(resource.getURI());
        String pyAbsolutePath = path.toString();

        ProcessBuilder pb = new ProcessBuilder(pythonPath, pyAbsolutePath);
        pb.inheritIO();
        pb.start();
    }
    
    public void runFastApiServer() throws IOException {
        String uvicornPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\Scripts\\uvicorn.exe";
        ClassPathResource resource = new ClassPathResource("static/py/festapi.py");
        Path path = Paths.get(resource.getURI());
        File workingDir = path.getParent().toFile();

        ProcessBuilder pb = new ProcessBuilder(
            uvicornPath,
            "festapi:app",
            "--reload",
            "--port",
            "8000"
        );
        pb.directory(workingDir);
        pb.inheritIO();
        pb.start();
    }
}
