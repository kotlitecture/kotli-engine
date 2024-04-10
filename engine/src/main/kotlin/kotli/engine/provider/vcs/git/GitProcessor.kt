package kotli.engine.provider.vcs.git

import kotli.engine.BaseFeatureProcessor
import kotli.engine.TemplateState
import kotli.engine.template.rule.WriteText

object GitProcessor : BaseFeatureProcessor() {

    const val ID = "vcs.git"

    override fun getId(): String = ID
    override fun isInternal(): Boolean = true

    override fun doApply(state: TemplateState) {
        state.onApplyRules(
            ".gitignore",
            WriteText(
                """
                *.apk
                *.ap_
                *.dex
                *.class
                local.properties
                .DS_Store
                _sandbox
                captures/
                *.hprof
                *.dll

                gradle/libs.versions.updates.toml
                appDistributionCredentials.json

                .gradle
                build/
                !gradle/wrapper/gradle-wrapper.jar
                !**/src/main/**/build/
                !**/src/test/**/build/

                ### STS ###
                .apt_generated
                .classpath
                .factorypath
                .project
                .settings
                .springBeans
                .sts4-cache
                bin/
                !**/src/main/**/bin/
                !**/src/test/**/bin/

                ### IntelliJ IDEA ###
                .idea
                *.iws
                *.iml
                *.ipr
                out/
                !**/src/main/**/out/
                !**/src/test/**/out/

                ### NetBeans ###
                /nbproject/private/
                /nbbuild/
                /dist/
                /nbdist/
                /.nb-gradle/

                ### VS Code ###
                .vscode/
            """.trimIndent()
            )
        )
    }
}