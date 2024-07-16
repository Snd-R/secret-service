rootProject.name = "secret-service"

includeBuild("../hkdf") {
    dependencySubstitution { substitute(module("at.favre.lib:hkdf")) }
}
