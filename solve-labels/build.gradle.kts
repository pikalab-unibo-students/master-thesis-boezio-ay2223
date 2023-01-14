plugins {
    `kotlin-mp`
    `kotlin-doc`
    `publish-on-maven`
    `publish-on-npm`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core"))
                api(project(":unify-labels"))
                api(project(":solve-classic"))
                api(project(":parser-core"))
            }
        }
    }
}

packageJson {
    dependencies = mutableMapOf(
        npmSubproject("unify-labels"),
        npmSubproject("solve-classic"),
    )
}
