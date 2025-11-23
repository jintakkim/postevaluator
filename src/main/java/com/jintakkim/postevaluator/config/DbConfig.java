package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.persistance.*;
import com.jintakkim.postevaluator.persistance.initialization.InitStatus;
import com.jintakkim.postevaluator.persistance.initialization.LabelDatabaseInitializer;
import com.jintakkim.postevaluator.persistance.initialization.TableDefinitionHashDatabaseInitializer;
import com.jintakkim.postevaluator.persistance.mapping.*;
import org.jdbi.v3.core.Jdbi;

import java.nio.file.Paths;

public class DbConfig {
    private static final String DEFAULT_FILE_NAME = "post_evaluator.db";
    private static final String DIR = "user.home";

    private volatile boolean initialized = false;
    private final DefinitionProperties definitionProperties;
    private final Jdbi jdbi;

    public final JdbiContext jdbiContext;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final LabelRepository labelRepository;
    public final SampleRepository sampleRepository;

    public DbConfig(String fileName, DefinitionProperties definitionProperties) {
        this.jdbi = Jdbi.create(createDbUrl(fileName));
        this.jdbiContext = new JdbiContext(jdbi);
        this.definitionProperties = definitionProperties;
        this.userRepository = new JdbiUserRepository(jdbiContext, definitionProperties.userDefinition());
        this.postRepository = new JdbiPostRepository(jdbiContext, definitionProperties.postDefinition());
        this.labelRepository = new JdbiLabelRepository(jdbiContext);
        this.sampleRepository = new JdbiSampleRepository(
                definitionProperties.userDefinition(),
                definitionProperties.postDefinition(),
                jdbiContext
        );
    }

    public DbConfig(DefinitionProperties definitionProperties) {
        this(DEFAULT_FILE_NAME, definitionProperties);
    }

    public void initialize() {
        if(!initialized) {
            registerRowMappers();
            initializeDbSchema();
            initialized = true;
        }
    }

    private void registerRowMappers() {
        jdbi.registerRowMapper(User.class, new UserMapper());
        jdbi.registerRowMapper(Post.class, new PostMapper());
        jdbi.registerRowMapper(Label.class, new LabelMapper());
        jdbi.registerRowMapper(UnlabeledSample.class, new UnlabeledSampleMapper());
        jdbi.registerRowMapper(LabeledSample.class, new LabeledSampleMapper());
    }

    private void initializeDbSchema() {
        InitStatus postInitStatus = new TableDefinitionHashDatabaseInitializer(jdbiContext, Post.name, definitionProperties.postDefinition()).initialize();
        InitStatus userInitStatus = new TableDefinitionHashDatabaseInitializer(jdbiContext, User.name, definitionProperties.userDefinition()).initialize();
        new LabelDatabaseInitializer(jdbiContext).initialize(postInitStatus, userInitStatus);
    }

    private static String createDbUrl(String fileName) {
        if(fileName == null || !fileName.endsWith(".db"))
            throw new IllegalArgumentException("파일 명은 .db로 끝나야 합니다.");
        String dir = System.getProperty(DIR);
        return "jdbc:sqlite:" + Paths.get(dir, fileName);
    }
}
