package com.soldesk.festival.runner;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;



@Component
public class PythonScriptRunner implements ApplicationRunner {

    @Value("${python.executable.path}")
    private String pythonPath;

    @Value("${uvicorn.executable.path}")
    private String uvicornPath;

    @Override
    public void run(ApplicationArguments args) throws Exception {

          System.out.println("DEBUG: Python Path Injected: " + pythonPath);
        System.out.println("DEBUG: Uvicorn Path Injected: " + uvicornPath);
        
        // 경로 값이 비어있거나 기본값이라면 실행을 중단하고 에러 메시지를 출력합니다.
        if (pythonPath == null || pythonPath.isEmpty() || uvicornPath == null || uvicornPath.isEmpty()) {
            throw new IllegalStateException("FATAL: Python/Uvicorn 경로가 application.properties에 설정되지 않았습니다 (@Value 로드 실패)");
        }
        // ===========

        runPythonScript();
        runFastApiServer();
    }

    public void runPythonScript() throws IOException {
        //String pythonPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\python.exe";
        ClassPathResource resource_f = new ClassPathResource("static/py/festivalApi.py");
        ClassPathResource resource_s = new ClassPathResource("static/py/news.py");
        
        //Path path1 = Paths.get(resource_f.getURI());
        //Path path2 = Paths.get(resource_s.getURI());

        String path1 = resource_f.getFile().getAbsolutePath();
        String path2 = resource_s.getFile().getAbsolutePath();

        ProcessBuilder pb1 = new ProcessBuilder(pythonPath, path1.toString());
        pb1.inheritIO();
        pb1.start();

        ProcessBuilder pb2 = new ProcessBuilder(pythonPath, path2.toString());
        pb2.inheritIO();
        pb2.start();
    }
    
    public void runFastApiServer() throws IOException {
        //String uvicornPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\Scripts\\uvicorn.exe";
        ClassPathResource resource = new ClassPathResource("static/py/runFastApi.py");
        //Path path = Paths.get(resource.getURI());
        File workingDir = resource.getFile().getParentFile();

        ProcessBuilder pb = new ProcessBuilder(
            uvicornPath,
            "runFastApi:app",
            "--reload",
            "--port",
            "8000"
        );
        pb.directory(workingDir);
        pb.inheritIO();
        pb.start();
    }
}
