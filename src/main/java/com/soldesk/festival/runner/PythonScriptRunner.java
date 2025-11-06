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

        // 로드된 경로의 공백을 제거합니다. (가장 중요한 수정)
        this.pythonPath = this.pythonPath.trim();
        this.uvicornPath = this.uvicornPath.trim();
        
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
<<<<<<< HEAD
=======
        //String pythonPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\python.exe";
>>>>>>> origin/main
        ClassPathResource resource_f = new ClassPathResource("static/py/festivalApi.py");
        ClassPathResource resource_s = new ClassPathResource("static/py/news.py");
        
        String path1 = resource_f.getFile().getAbsolutePath();
        String path2 = resource_s.getFile().getAbsolutePath();

<<<<<<< HEAD
        // 수정된 pythonPath(공백 제거됨) 사용
        ProcessBuilder pb1 = new ProcessBuilder(pythonPath, path1.toString()); 
        pb1.inheritIO();
        pb1.start();

        // 수정된 pythonPath(공백 제거됨) 사용
        ProcessBuilder pb2 = new ProcessBuilder(pythonPath, path2.toString());
=======
        ProcessBuilder pb1 = new ProcessBuilder("python", path1.toString());
        pb1.inheritIO();
        pb1.start();

        ProcessBuilder pb2 = new ProcessBuilder("python", path2.toString());
>>>>>>> origin/main
        pb2.inheritIO();
        pb2.start();
    }
    
    public void runFastApiServer() throws IOException {
<<<<<<< HEAD
=======
        //String uvicornPath = "C:\\Users\\soldesk\\AppData\\Local\\Programs\\Python\\Python314\\Scripts\\uvicorn.exe";
>>>>>>> origin/main
        ClassPathResource resource = new ClassPathResource("static/py/runFastApi.py");
        File workingDir = resource.getFile().getParentFile();

        ProcessBuilder pb = new ProcessBuilder(
<<<<<<< HEAD
            // 수정된 uvicornPath(공백 제거됨) 사용
            uvicornPath, 
=======
            "uvicorn",
>>>>>>> origin/main
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