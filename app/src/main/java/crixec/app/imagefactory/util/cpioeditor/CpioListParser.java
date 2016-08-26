package crixec.app.imagefactory.util.cpioeditor;

/**
 * Created by crixec on 16-8-22.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import crixec.app.imagefactory.core.Debug;

public class CpioListParser {
    //	dir . 0777 0 0
//	file fstab.qcom ramdisk/fstab.qcom 0640 0 0
//	slink charger /sbin/healthd 0777 0 0
//	dir data 0771 0 0
//
    public static final String ENTITY_FILE = "file";
    public static final String ENTITY_DIR = "dir";
    public static final String ENTITY_SYMLINK = "slink";
    public static final String ENTITY_UNKNOW = "unknow";


    private File cpioFile;
    private List<CpioEntity> entities = new ArrayList<>();
    private String TAG = "CpioListParser";

    private CpioListParser(File cpioFile) {
        this.cpioFile = cpioFile;
    }

    private CpioDirEntity parseDirectory(String line) {
        CpioDirEntity entity = new CpioDirEntity();
        String[] content = line.split(" ");
        if (content.length != 5) {
            entity.setType(ENTITY_UNKNOW);
        } else {
            entity.setType(ENTITY_DIR);
        }
        if (!entity.getType().equals(ENTITY_UNKNOW)) {
            entity.setTarget(content[1].trim());
            entity.setPermission(content[2].trim());
            entity.setGid(content[3].trim());
            entity.setUid(content[4].trim());
        }
        return entity;
    }

    private CpioEntity parseRegular(String line) {
        CpioEntity entity = new CpioEntity();
        String[] content = line.split(" ");
        String prefix = content[0];
        if (content.length != 6) {
            entity.setType(ENTITY_UNKNOW);
        } else {
            if (prefix.equals(ENTITY_FILE)) {
                entity.setType(ENTITY_FILE);
            } else if (prefix.equals(ENTITY_DIR)) {
                entity.setType(ENTITY_DIR);
            } else if (prefix.equals(ENTITY_SYMLINK)) {
                entity.setType(ENTITY_SYMLINK);
            } else {
                entity.setType(ENTITY_UNKNOW);
            }
            if (!entity.getType().equals(ENTITY_UNKNOW)) {
                entity.setTarget(content[1].trim());
                entity.setSource(content[2].trim());
                entity.setPermission(content[3].trim());
                entity.setGid(content[4].trim());
                entity.setUid(content[5].trim());
            }
        }
        return entity;
    }

    private CpioEntity parseLine(String line) {
        return line.split(" ")[0].equals(ENTITY_DIR) ? parseDirectory(line) : parseRegular(line);

    }

    public List<CpioEntity> getEntities() {
        return entities;
    }

    public boolean isLineValid(String line) {
        return !parseLine(line).getType().equals(ENTITY_UNKNOW);
    }

    public void parse() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cpioFile)));
        try {
            entities.clear();
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.equals("")) continue;
                CpioEntity entity = parseLine(line.replace("  ", " "));
                if (entity.getType().equals(ENTITY_UNKNOW)) {
                    Debug.e(TAG, "Parsing cpio list failed with line: " + line);
                } else {
                    entities.add(entity);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        Collections.sort(entities, new Comparator<CpioEntity>() {
            @Override
            public int compare(CpioEntity lhs, CpioEntity rhs) {
                if (lhs.getType().equals(ENTITY_DIR) && rhs.getType().equals(ENTITY_FILE)) {
                    return -1;
                }
                if (lhs.getType().equals(ENTITY_FILE) && rhs.getType().equals(ENTITY_DIR)) {
                    return 1;
                }
                if (lhs.getType().equals(ENTITY_SYMLINK) && !rhs.getType().equals(ENTITY_SYMLINK)) {
                    return 1;
                }
                if (!lhs.getType().equals(ENTITY_SYMLINK) && rhs.getType().equals(ENTITY_SYMLINK)) {
                    return -1;
                }
                return lhs.getTarget().compareToIgnoreCase(rhs.getTarget());
            }
        });
    }

    public static CpioListParser fromFile(File file) {
        return new CpioListParser(file);
    }
}