package com.cvt.backend_demo.service;


import com.cvt.backend_demo.dto.UserDTO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReaderService {

    public List<UserDTO> readCsv(String filePath) throws IOException {
        List<UserDTO> users = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            boolean firstRow = true;

            for (String[] row : rows) {
                if (firstRow) { // ✅ Skip CSV header
                    firstRow = false;
                    continue;
                }

                if (row.length < 5) continue; // ✅ Skip invalid rows

                UserDTO user = new UserDTO(
                        row[0], // ✅ username
                        row[1], // ✅ email
                        row[2], // ✅ firstName
                        row[3], // ✅ lastName
                        row[4], // ✅ organization
                        row[5]  // ✅ password
                );

                users.add(user);
            }
        } catch (CsvException e) {
            throw new IOException("❌ Error reading CSV file", e);
        }
        return users;
    }
}
