#!/usr/bin/env -S jbang
//JAVA 11+
//JAVAC_OPTIONS -Xlint:none
//JAVA_OPTIONS --enable-preview
//DEPS info.picocli:picocli:4.5.1
//DEPS io.kubernetes:client-java:9.0.0
//DEPS org.slf4j:slf4j-simple:1.7.30

import picocli.CommandLine;
import picocli.CommandLine.*;
import java.util.regex.Pattern;
import java.util.concurrent.Callable;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;

@Command(name = "Simple kubectl plugin", mixinStandardHelpOptions = true, version = "1",
    description = "Gets Pods names and other stuff")
class PluginTest implements Callable<Integer> {

  @CommandLine.Option(names = { "-n", "--namespace" }, description = "The @|bold namespace|@",
      paramLabel = "NAMESPACE")
  String namespace = null;

  @Parameters(index = "0", description = "Regex pattern", paramLabel = "NAMEPATTERN",
      arity = "0..1")
  Pattern namePattern = null;

  public static void main(String... args) {
    System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
    int exitCode = new CommandLine(new PluginTest()).execute(args);
    System.exit(exitCode);
  }

  private void printlnAnsi(String msg) {
    System.out.println(CommandLine.Help.Ansi.AUTO.string(msg));
  }

  @Override
  public Integer call() {
    try {
      ApiClient client = Config.defaultClient();
      Configuration.setDefaultApiClient(client);

      CoreV1Api api = new CoreV1Api();
      V1PodList list = namespace == null
          ? api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null)
          : api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null);
      list.getItems()
          .stream()
          .filter(namePattern != null
            ? pod -> namePattern.asMatchPredicate().test(pod.getMetadata().getName())
            : pod -> true)
          .forEach(pod -> printlnAnsi("@|green Name:|@ "
            + pod.getMetadata().getName()
            + "\t@|green SelfLink:|@ "
            + pod.getMetadata().getSelfLink()));
      return 0;
    } catch (Exception ex) {
      ex.printStackTrace();
      return 1;
    }
  }
}
