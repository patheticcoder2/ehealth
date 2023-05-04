package ehealth.group1.backend;

import ca.uhn.fhir.context.FhirContext;
import ehealth.group1.backend.jely.JELYmaster.de.fau.mad.jely.*;
import ehealth.group1.backend.jely.JELYmaster.de.fau.mad.jely.detectors.HeartbeatDetector;
import ehealth.group1.backend.jely.JELYmaster.de.fau.mad.jely.io.FileLoader;
import ehealth.group1.backend.generators.IDStringGenerator;
import ehealth.group1.backend.helper.dataloaders.TestDataLoader;
import ehealth.group1.backend.repositories.DataRepository;
import ehealth.group1.backend.repositories.SettingsRepository;
import ehealth.group1.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@EntityScan("ehealth.group1.backend.*")
public class BackendApplication {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final FhirContext ctx;
  private final SettingsRepository settingsRepository;
  private final DataRepository dataRepository;
  private final UserRepository userRepository;
  private final TestDataLoader testDataLoader;

  public BackendApplication(FhirContext ctx, SettingsRepository settingsRepository, DataRepository dataRepository, UserRepository userRepository, TestDataLoader testDataLoader) {
    this.ctx = ctx;
    this.settingsRepository = settingsRepository;
    this.dataRepository = dataRepository;
    this.userRepository = userRepository;
    this.testDataLoader = testDataLoader;
  }

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }

  /*
    Bean for getting and interpreting command line arguments handed via -Dspring-boot.run.arguments="##ARGUMENT##"
  */
  @Bean
  public CommandLineRunner commandLineRunnerBean() {
    return (args) -> {

      if(args.length > 0) {
        LOGGER.info("Starting server with command line arguments: " + Arrays.toString(args));

        for (String arg : args) {
          // Code to execute if argument is present
          switch (arg) {
            case "test":
              testDataLoader.exec();
              //execHexIDTests();
              execTests();
              jelyTest();
          }
        }
      }
    };
  }

  private void execHexIDTests() {
    for(int i = 0; i < 5; i++) {
      String hexString = IDStringGenerator.getNewIDString();
      LOGGER.warn("Generated hex id: " + hexString);
    }
  }

/*
  private void execDBTests2() {
    User user = new User(57L, "NewUser", "ExampleAddress", 42L, true, "pwd");
    LOGGER.info("Creating new user: " + user);

    User newUser = userRepository.save(user);

    LOGGER.info("User created: " + userRepository.findById(newUser.getId()));


    User user2 = new User(userId, "NewUser2", "ExampleAddress2", 43L, false, "pwd2");

    dataDao.updateUser(user2);

    LOGGER.info("User updated: " + dataDao.searchUser(user2).get(0));

    User user3 = new User(null, "NewUser2", "ExampleAddress2", 43L, false, "pwd2");

    List<User> searchResult = dataDao.searchUser(user3);

    LOGGER.info("User search result: " + searchResult.get(0));
    LOGGER.info("Search result list length: " + searchResult.size());

    dataDao.deleteUser(user2);

    LOGGER.info("User exists?: " + dataDao.userExists(user2.getId()));
  }

  private void execDBTests() {
    LOGGER.warn("Executing database tests...");

    if(true) {
      return;
    }

    Settings settings = settingsRepository.findByUserId(0L);
    ECGStateHolderSettings stateholder = settings.getEcgStateHolderSettings();
    ECGAnalysisSettings analysis = settings.getEcgAnalysisSettings();

    User user = userRepository.findById(0L).orElseThrow(() -> new PersistenceException("Error while retrieving user from userRepository"));

    StringBuilder s = new StringBuilder();

    s.append("\n\n");
    s.append("  Settings - id: ").append(settings.getId()).append("\n");
    s.append("  Settings - user_id: ").append(settings.getUserId()).append("\n");
    s.append("  Stateholder - id: ").append(stateholder.getId()).append("\n");
    s.append("  Stateholder - user_id: ").append(stateholder.getUser_id()).append("\n");
    s.append("  Stateholder - iterations_transition: ").append(stateholder.getIterationsToStateTransition()).append("\n");
    s.append("  Stateholder - iterations_emergency: ").append(stateholder.getIterationsToEmergencyCall()).append("\n");
    s.append("  Analysis - id: ").append(analysis.getId()).append("\n");
    s.append("  Analysis - user_id: ").append(analysis.getUser_id()).append("\n");
    s.append("  Analysis - maxDeviations: ").append(analysis.getMaxDeviation()).append("\n");
    s.append("  Analysis - maxDeviations_num: ").append(analysis.getMaxDeviationNum()).append("\n");
    s.append("--------------------------------\n");
    s.append("  User - id: ").append(user.getId()).append("\n");
    s.append("  User - name: ").append(user.getName()).append("\n");
    s.append("  User - address: ").append(user.getAddress()).append("\n");
    s.append("  User - phone: ").append(user.getPhone()).append("\n");
    s.append("  User - emergency: ").append(user.getEmergency()).append("\n");
    s.append("  User - password: ").append(user.getPassword()).append("\n");

    LOGGER.info("Data:" + s);
  }

  private void execTests() {
    LOGGER.warn("Executing quick startup tests...");

    LOGGER.info("Creating fhir contexts...");

    FhirContext c1 = ctx;
    FhirContext c2 = FhirContext.forR5Cached();
    FhirContext c3 = FhirContext.forR5Cached();
    FhirContext c4 = FhirContext.forR5();
    FhirContext c5 = FhirContext.forR5Cached();

    LOGGER.info("Context c1: " + c1.hashCode());
    LOGGER.info("Context c2: " + c2.hashCode());
    LOGGER.info("Context c3: " + c3.hashCode());
    LOGGER.info("Context c4: " + c4.hashCode());
    LOGGER.info("Context c5: " + c5.hashCode());

    LOGGER.info("Startup tests finished.");
  }

  private void jelyTest() {
    Ecglib.setDebugMode(true);
    LOGGER.info("EcgLib.isDebugMode? " + Ecglib.isDebugMode());
    Ecg ecgFile = FileLoader.loadKnownEcgFile("src/main/java/ehealth/group1/backend/jely/testfiles/jelyecg4.csv",
            LeadConfiguration.SINGLE_UNKNOWN_LEAD, 125);
    LOGGER.info("\n\necgFile: " + ecgFile.toString() + "\n\n");
    HeartbeatDetector detector = new HeartbeatDetector(ecgFile, (HeartbeatDetector.HeartbeatDetectionListener) null);
    ArrayList<Heartbeat> beatList = detector.findHeartbeats();

    LOGGER.info("\n\nBeatlist: " + Arrays.toString(beatList.toArray()) + "\n\n");

    for(Heartbeat h : beatList) {
      QrsComplex qrs = h.getQrs();
      int rPeak = qrs.getRPosition();
      LOGGER.info("\n\nHeartbeat: " + h.toString() + "\nQRS: " + qrs.toString() + "\nR-peak: " + rPeak + "\n\n");
    }
  }
 */
}
