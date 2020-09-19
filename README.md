# kubectl-java-test

Simple kubectl plugin written in Java language and runned using [jbang](https://jbang.dev/).

**Please note that this isn't a true kubectl plugin, this is just sample code!**

java-test plugin gets pod names and other information, you can limit the namespace scope or use a regex pattern to search for pod names.

```bash
$ kubectl java-test -h
Usage: Simple kubectl plugin [-hV] [-n=NAMESPACE] [NAMEPATTERN]
Gets Pods names and other stuff
      [NAMEPATTERN]   Regex pattern
  -h, --help          Show this help message and exit.
  -n, --namespace=NAMESPACE
                      The namespace
  -V, --version       Print version information and exit.
```

## Install

Install [jbang](https://jbang.dev/download) and then:

```bash
# Note: the .java extension must be omitted in order to make kubectl able to recognize the plugin
# See https://kubernetes.io/docs/tasks/extend-kubectl/kubectl-plugins/#naming-a-plugin
curl -Lo kubectl-java_test https://github.com/filippobuletto/kubectl-java-test/releases/latest/download/kubectl-java_test.java
sudo install -m755 kubectl-java_test /usr/local/bin
rm kubectl-java_test
```

## Usage

```
kubectl java-test -n <namespace> <pod_name_regex>
```

![example](/example.png?raw=true "Example")
