package manos.examples.gofFunctional;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class ChainOfResponsibility {
    private static String TEXT_FILE_CONTENT_PREFIX = "Text file: ";
    private static String PRESENTATION_FILE_CONTENT_PREFIX = "Presentation file: ";
    private static String AUDIO_FILE_CONTENT_PREFIX = "Audio file: ";
    private static String VIDEO_FILE_CONTENT_PREFIX = "Video file: ";
    private enum Type{TEXT, PRESENTATION, AUDIO, VIDEO};
    private class File{
        private final Type type;
        private final String content;

        public File(Type type, String content) {
            this.type = type;
            this.content = content;
        }

        public Type getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }

    public Optional<String> parseText(File file) {
        return Optional.ofNullable(file)
                .filter(f -> f.getType() == Type.TEXT )
                .map(f -> TEXT_FILE_CONTENT_PREFIX + f.getContent());
    }

    public Optional<String> parsePresentation(File file) {
        return Optional.ofNullable(file)
                .filter(f -> f.getType() == Type.PRESENTATION )
                .map(f -> PRESENTATION_FILE_CONTENT_PREFIX + f.getContent());
    }

    public Optional<String> parseAudio(File file) {
        return Optional.ofNullable(file)
                .filter(f -> f.getType() == Type.AUDIO )
                .map(f -> AUDIO_FILE_CONTENT_PREFIX + f.getContent());
    }

    public Optional<String> parseVideo(File file) {
        return Optional.ofNullable(file)
                .filter(f -> f.getType() == Type.VIDEO )
                .map(f -> VIDEO_FILE_CONTENT_PREFIX + f.getContent());
    }


    @Test
    public void testChainOfResponsibilty(){
        String fileContent = "MANOS_TZAGKARAKIS_CHAIN_OF_RESPONSIBILITY_WITH_LAMBDA_EXAMPLE";
        File fileToChain = new File(Type.TEXT, fileContent);

        String resultContent =
            Stream.<Function<File, Optional<String>>>builder()
                    .add(this::parseText)
                    .add(this::parsePresentation)
                    .add(this::parsePresentation)
                    .add(this::parseVideo)
                    .build()
                    .map(f -> f.apply(fileToChain))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElseThrow(()->new RuntimeException("Unknown File Type"));
        Assert.assertTrue(resultContent.equals(TEXT_FILE_CONTENT_PREFIX+fileContent));
    }

}
