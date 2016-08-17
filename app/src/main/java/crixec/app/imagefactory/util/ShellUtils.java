package crixec.app.imagefactory.util;
// Copyright (C) 2016 Crixec
// This file is part of Quick-Android.
// https://github.com/Crixec/Quick-Android
//
// Quick-Android is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Quick-Android is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.core.Debug;

public class ShellUtils {
    private static int hasRoot = 123;
    private static String TAG = "ShellUtils";

    public interface Result {
        void onStdout(String text);

        void onStderr(String text);

    }

    private interface Output {
        void output(String text);
    }

    public static class OutputReader extends Thread {
        private Output output = null;
        private BufferedReader reader = null;
        private boolean isRunning = false;

        public OutputReader(BufferedReader reader, Output output) {
            this.output = output;
            this.reader = reader;
            this.isRunning = true;
        }

        public void close() {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            String line;
            while (isRunning) {
                try {
                    line = reader.readLine();
                    if (line != null)
                        output.output(line);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
        }

        public void cancel() {
            synchronized (this) {
                isRunning = false;
                this.notifyAll();
            }
        }
    }

    private static int exec(final String sh, final List<String> cmds, final Result result) {
        Process process;
        DataOutputStream stdin = null;
        OutputReader stdout = null;
        OutputReader stderr = null;
        int resultCode = -1;

        try {
            process = Runtime.getRuntime().exec(sh);
            stdin = new DataOutputStream(process.getOutputStream());
            if (result != null) {
                stdout = new OutputReader(new BufferedReader(new InputStreamReader(process.getInputStream())),
                        new Output() {
                            @Override
                            public void output(String text) {
                                // TODO Auto-generated method stub
                                result.onStdout(text);
                            }
                        });
                stderr = new OutputReader(new BufferedReader(new InputStreamReader(process.getErrorStream())),
                        new Output() {
                            @Override
                            public void output(String text) {
                                // TODO Auto-generated method stub
                                result.onStderr(text);
                            }
                        });
                stdout.start();
                stderr.start();
            }
            for (String cmd : cmds) {
                Debug.i(TAG, cmd);
                stdin.writeBytes(cmd);
                stdin.writeBytes("\n");
                stdin.flush();
            }
            stdin.writeBytes("exit $?\n");
            stdin.flush();
            resultCode = process.waitFor();
            Debug.i(TAG, "RESULT_CODE=" + resultCode);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            return resultCode;
        } finally {
            try {
                stdout.cancel();
                stderr.cancel();
                stdin.close();
                stdout.close();
                stderr.close();
            } catch (Exception e) {

            }
        }
    }

    public static int exec(final List<String> cmds, final Result result, final boolean isRoot) {
        String sh = isRoot ? "su" : "sh";
        return exec(sh, cmds, result);
    }

    public static int exec(final String cmd, final Result result, boolean isRoot) {
        List<String> cmds = new ArrayList<String>();
        cmds.add(cmd);
        return exec(cmds, result, isRoot);
    }

    public static int exec(final String cmd) {
        return exec(cmd, null, false);
    }

    public static int execRoot(final String cmd) {
        return exec(cmd, null, true);
    }

    public static int execRoot(final String cmd, final Result result) {
        return exec(cmd, result, true);
    }

    public static boolean hasRootPermission() {
        if (hasRoot == 123) {
            ShellUtils.exec("id", new ShellUtils.Result() {
                @Override
                public void onStdout(String text) {
                    Debug.i(TAG, "Check root permission : " + text);
                    hasRoot = text.contains("uid=0") ? 0 : 1;
                }

                @Override
                public void onStderr(String text) {

                }
            }, true);
        }
        return hasRoot == 0;
    }
}