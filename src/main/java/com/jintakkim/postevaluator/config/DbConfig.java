package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.config.properties.DefinitionProperties;
import com.jintakkim.postevaluator.Label;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.LabeledSample;
import com.jintakkim.postevaluator.User;
import com.jintakkim.postevaluator.persistance.*;
import com.jintakkim.postevaluator.persistance.initialization.TableDefinitionHashDatabaseInitializer;
import org.jdbi.v3.core.Jdbi;

import java.nio.file.Paths;

public class DbConfig {
    private static final String DEFAULT_FILE_NAME = "post_evaluator.db";
    private static final String DIR = "user.home";

    private final DefinitionProperties definitionProperties;
    public final Jdbi jdbi;

    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final LabelRepository labelRepository;


    public DbConfig(String fileName, DefinitionProperties definitionProperties) {
        this.jdbi = Jdbi.create(createDbUrl(fileName));
        this.definitionProperties = definitionProperties;
        registerRowMappers();
        initializeDbSchema();
        this.userRepository = new JdbiUserRepository(jdbi, definitionProperties.userDefinition);
        this.postRepository = new JdbiPostRepository(jdbi, definitionProperties.postDefinition);
        this.labelRepository = new JdbiLabelRepository(jdbi);
    }

    public DbConfig(DefinitionProperties definitionProperties) {
        this(DEFAULT_FILE_NAME, definitionProperties);
    }

    private void registerRowMappers() {
        jdbi.registerRowMapper(User.class, new UserMapper());
        jdbi.registerRowMapper(Post.class, new PostMapper());
        jdbi.registerRowMapper(Label.class, new LabelMapper());
        jdbi.registerRowMapper(LabeledSample.class, new LabeledSampleMapper());
    }

    private void initializeDbSchema() {
        new TableDefinitionHashDatabaseInitializer(jdbi, Post.name, definitionProperties.postDefinition).initialize();
        new TableDefinitionHashDatabaseInitializer(jdbi, User.name, definitionProperties.userDefinition).initialize();
    }

    private static String createDbUrl(String fileName) {
        if(fileName == null || !fileName.endsWith(".db"))
            throw new IllegalArgumentException("파일 명은 .db로 끝나야 합니다.");
        String dir = System.getProperty(DIR);
        return "jdbc:sqlite:" + Paths.get(dir, fileName);
    }
}
