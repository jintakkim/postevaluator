package com.jintakkim.postevaluator.test;

import com.jintakkim.postevaluator.Label;
import com.jintakkim.postevaluator.LabeledSampleProvider;
import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.User;
import com.jintakkim.postevaluator.generation.PostGenerator;
import com.jintakkim.postevaluator.generation.UserGenerator;
import com.jintakkim.postevaluator.labeling.Labeler;
import com.jintakkim.postevaluator.persistance.DatasetManager;
import com.jintakkim.postevaluator.persistance.LocalFileDbIntegrationTest;
import com.jintakkim.postevaluator.persistance.SetupStrategy;
import com.jintakkim.postevaluator.test.fixture.PostFixture;
import com.jintakkim.postevaluator.test.fixture.UserFixture;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

import java.util.List;

/**
 * 데이터 형식만 갖춘 의미적으로 아무런 정보가 없는 간단한 fixture가 db에 저장된 상태로 제공된다.
 */
public class SimpleFixtureAppliedTest extends LocalFileDbIntegrationTest {
    protected static User USER_1;
    protected static User USER_2;
    protected static User USER_3;
    protected static User USER_4;
    protected static int APPLIED_USER_SIZE = 4;

    protected static Post POST_1;
    protected static Post POST_2;
    protected static Post POST_3;
    protected static Post POST_4;
    protected static int APPLIED_POST_SIZE = 4;

    @BeforeAll
    static void setup() {
        USER_1 = userRepository.save(UserFixture.DEFINITION_1.USER_1);
        USER_2 = userRepository.save(UserFixture.DEFINITION_1.USER_2);
        USER_3 = userRepository.save(UserFixture.DEFINITION_1.USER_3);
        USER_4 = userRepository.save(UserFixture.DEFINITION_1.USER_4);
        POST_1 = postRepository.save(PostFixture.DEFINITION_1.POST_1);
        POST_2 = postRepository.save(PostFixture.DEFINITION_1.POST_2);
        POST_3 = postRepository.save(PostFixture.DEFINITION_1.POST_3);
        POST_4 = postRepository.save(PostFixture.DEFINITION_1.POST_4);
        labelRepository.saveAll(List.of(
                new Label(null, POST_1.id(), USER_1.id(), 0.8, "no reason"),
                new Label(null, POST_2.id(), USER_1.id(), 0.7, "no reason"),
                new Label(null, POST_3.id(), USER_1.id(), 0.7, "no reason"),
                new Label(null, POST_4.id(), USER_1.id(), 0.7, "no reason"),

                new Label(null, POST_1.id(), USER_2.id(), 0.8, "no reason"),
                new Label(null, POST_2.id(), USER_2.id(), 0.7, "no reason"),
                new Label(null, POST_3.id(), USER_2.id(), 0.7, "no reason"),
                new Label(null, POST_4.id(), USER_2.id(), 0.7, "no reason"),

                new Label(null, POST_1.id(), USER_3.id(), 0.8, "no reason"),
                new Label(null, POST_2.id(), USER_3.id(), 0.7, "no reason"),
                new Label(null, POST_3.id(), USER_3.id(), 0.7, "no reason"),
                new Label(null, POST_4.id(), USER_3.id(), 0.7, "no reason"),

                new Label(null, POST_1.id(), USER_4.id(), 0.8, "no reason"),
                new Label(null, POST_2.id(), USER_4.id(), 0.7, "no reason"),
                new Label(null, POST_3.id(), USER_4.id(), 0.7, "no reason"),
                new Label(null, POST_4.id(), USER_4.id(), 0.7, "no reason")
        ));
    }

    protected LabeledSampleProvider createLabeledSampleProvider(int targetPostSize, int targetUserSize, SetupStrategy setupStrategy) {
        if(
                (setupStrategy == SetupStrategy.CLEAR || setupStrategy == SetupStrategy.REUSE_STRICT) &&
                        (targetPostSize != APPLIED_POST_SIZE || targetUserSize != APPLIED_USER_SIZE)
        ) {
            throw new IllegalArgumentException("SetupStrategy.CLEAR or REUSE_STRICT일때는 데이터 정합성을 위해" +
                    " SimpleFixtureAppliedTest에서 제공된 데이터셋 사이즈와 맞춰야합니다.");
        }
        if(setupStrategy == SetupStrategy.REUSE_PARTITION && targetPostSize > APPLIED_USER_SIZE) {
            throw new IllegalArgumentException("SetupStrategy.REUSE_PARTITION일때는 targetUserSize가 데이터 정합성을 위해 " +
                    "SimpleFixtureAppliedTest에서 제공된 데이터셋 사이즈인 " + APPLIED_USER_SIZE + "보다 이하여야 합니다.");
        }
        if(setupStrategy == SetupStrategy.REUSE_PARTITION && targetPostSize > APPLIED_POST_SIZE) {
            throw new IllegalArgumentException("SetupStrategy.REUSE_PARTITION일때는 targetPostSize가 데이터 정합성을 위해 " +
                    "SimpleFixtureAppliedTest에서 제공된 데이터셋 사이즈인 " + APPLIED_POST_SIZE + "보다 이하여야 합니다.");
        }
        DatasetManager datasetManager = new DatasetManager(
                jdbiContext,
                targetUserSize,
                targetPostSize,
                setupStrategy,
                postRepository,
                userRepository,
                labelRepository,
                sampleRepository,
                Mockito.mock(UserGenerator.class),
                Mockito.mock(PostGenerator.class),
                Mockito.mock(Labeler.class)
        );
        TestRefectionUtils.setField(DatasetManager.class, datasetManager, "isInitialized", true);
        return Mockito.spy(datasetManager);
    };
}
