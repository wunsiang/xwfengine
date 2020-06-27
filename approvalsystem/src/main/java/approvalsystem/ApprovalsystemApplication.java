package approvalsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("approvalsystem.dao")
public class ApprovalsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApprovalsystemApplication.class, args);
    }

}
