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
        //String pythonPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\python.exe";
        ClassPathResource resource_f = new ClassPathResource("static/py/festivalApi.py");
        ClassPathResource resource_n = new ClassPathResource("static/py/news.py");
        ClassPathResource resource_a = new ClassPathResource("static/py/analytics.py");
        ClassPathResource resource_d = new ClassPathResource("static/py/festivalData.py");

        
        Path path1 = Paths.get(resource_f.getURI());
        Path path2 = Paths.get(resource_n.getURI());
        Path path3 = Paths.get(resource_a.getURI());
        Path path4 = Paths.get(resource_d.getURI());

        ProcessBuilder pb1 = new ProcessBuilder("python", path1.toString());
        pb1.inheritIO();
        pb1.start();

        ProcessBuilder pb2 = new ProcessBuilder("python", path2.toString());
        pb2.inheritIO();
        pb2.start();

        ProcessBuilder pb3 = new ProcessBuilder("python", path3.toString());
        pb3.inheritIO();
        pb3.start();

        ProcessBuilder pb4 = new ProcessBuilder("python", path4.toString());
        pb4.inheritIO();
        pb4.start();

        System.out.println("path4 = " + path4);
    }
    
    public void runFastApiServer() throws IOException {
        //String uvicornPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\Scripts\\uvicorn.exe";
        ClassPathResource resource = new ClassPathResource("static/py/runFastApi.py");
        Path path = Paths.get(resource.getURI());
        File workingDir = path.getParent().toFile();

        ProcessBuilder pb = new ProcessBuilder(
            "uvicorn",
            "runFastApi:app",
            "--reload",
            "--port",
            "8000",
            "--host",
            "0.0.0.0"
        );
        pb.directory(workingDir);
        pb.inheritIO();
        pb.start();
    }
}
