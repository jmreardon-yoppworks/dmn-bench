package example

import org.kie.api.KieServices
import org.kie.dmn.api.core.DMNResult
import org.kie.dmn.api.core.DMNRuntime
import org.kie.internal.io.ResourceFactory
import org.kie.util.maven.support.ReleaseIdImpl
import org.openjdk.jmh.annotations._

import java.util.concurrent.TimeUnit
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.jdk.CollectionConverters.SeqHasAsJava

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
class DmnListFilterBench {

  private val container = createContainer("list_filtering.dmn")
  private val kieSession = container.newKieSession()

  private val dmnRuntime = kieSession.getKieRuntime(classOf[DMNRuntime])
  private val dmnModel = dmnRuntime
    .getModel(
      "https://kiegroup.org/dmn/_BDB6D3FE-834D-483B-A8DD-AB6DBA9BAD75",
      "list_filtering"
    )

  private val dmnContext = dmnRuntime.newContext
  private val reports = (1 to 10).map { i =>

    Map("confirmed" -> Boolean.box(i % 2 == 0), "value" -> i.toString).asJava

  }.asJava
  dmnContext.set("reports", reports)

  @Benchmark def evaluate1Function: DMNResult =
    dmnRuntime.evaluateByName(dmnModel, dmnContext, "FilterList1Func")

  @Benchmark def evaluate2Functions: DMNResult =
    dmnRuntime.evaluateByName(dmnModel, dmnContext, "FilterList2Func")

  @Benchmark def evaluateBoolItem: DMNResult =
    dmnRuntime.evaluateByName(dmnModel, dmnContext, "FilterListBoolItem")

  @Benchmark def evaluateBool: DMNResult =
    dmnRuntime.evaluateByName(dmnModel, dmnContext, "FilterListBool")

  private def createContainer(resourcePath: String) = {
    val paths = resourcePath.split(',')

    // there is always at least one
    def artifactId =
      paths.head // scalastyle:ignore
        .replace('.', '_')
        .replace('/', '_')

    val kieServices = KieServices.get()

    val kieFileSystem = kieServices.newKieFileSystem()

    paths.foreach { path =>
      kieFileSystem.write(ResourceFactory.newClassPathResource(path))
    }
    kieFileSystem.generateAndWritePomXML(
      new ReleaseIdImpl("com.jmreardon", artifactId, "1.0.0")
    )

    val kieBuilder = kieServices.newKieBuilder(kieFileSystem)
    kieBuilder.buildAll()

    val kieModule = kieBuilder.getKieModule
    kieServices.newKieContainer(kieModule.getReleaseId)
  }
}
