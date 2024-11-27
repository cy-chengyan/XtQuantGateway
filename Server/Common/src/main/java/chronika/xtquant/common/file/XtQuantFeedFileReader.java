package chronika.xtquant.common.file;

import com.opencsv.CSVParser;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class XtQuantFeedFileReader {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(XtQuantFeedFileReader.class);

    private final String filePath;

    public XtQuantFeedFileReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String[]> load() {
        List<String[]> ret = new ArrayList<>();
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(this.filePath);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            CSVParser csvParser = new CSVParser();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (StringUtils.hasLength(line)) {
                    ret.add(csvParser.parseLine(line));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed load feed file: " + filePath, e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    log.warn("Failed to close BufferedReader for file {}", filePath, e);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.warn("Failed to close FileInputStream for file {}", filePath, e);
                }
            }
        }
        return ret;
    }

}
