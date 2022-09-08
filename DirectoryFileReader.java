package com.jpomykala.sentiment.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class DirectoryFileReader {

  public List<String> readTextFilesInDirectory(Path path) throws IOException {
    try (Stream<Path> paths = Files.walk(path)) {

      List<Path> pathList = paths
              .filter(Files::isRegularFile)
              .collect(Collectors.toList());
      log.info("Found {} files", pathList.size());
      List<String> readFiles = pathList.stream()
              .map(p -> {
                try {
                  return Files.readString(p);
                } catch (IOException e) {
                  return null;
                }
              })
              .filter(Objects::nonNull)
              .collect(Collectors.toList());

      log.info("Finished reading {} files", pathList.size());
      return readFiles;
    }
  }
}
