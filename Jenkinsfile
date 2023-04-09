#!groovy
/*
Jenkins CI pipeline
===================
See jenkins/Jenkinsfile_mavenDevOpsPipeline_template for all possible configuration arguments.

*/
library 'global-alm-pipeline-library'
mavenDevOpsPipeline (
        releaseCandidateBranchPrefix: 'candidate-',
        hotfixBranchPrefix: 'hf-',
        agent: 'jdk11',
        masterBranch: 'master',
        integrationBranch: 'development',
        deployArtifactId: 'pagonxt-onetradefinance-external-backend',
        deployArtifactPackaging: 'jar',
        build: [
               goal: '-Ponly-external-app,environment-santander clean verify', // maven command
        ],
        nexus: [
                arguments: '-Ponly-external-app,environment-santander -Dmaven.test.skip=true', // Maven deploy arguments
        ],
        multi_registry: [
          [
            credentialsId: 'ocp4-gts-mimacom-dev',
            registry: 'registry.gts.dev.weu.azure.paas.cloudcenter.corp',
            image: 'gts-mimacom-dev/immutable-test'
          ],
          [
            credentialsId: 'ocp4-gts-mimacom-dev',
            registry: 'registry.gts.pre.weu.azure.paas.cloudcenter.corp',
            image: 'gts-mimacom-dev/immutable-test'
          ]
        ],
        docker: [
            kanikoArgs: '--cache=false --build-arg HTTP_PROXY=${http_proxy} --build-arg IMAGE_TARGET_VERSION=${VERSION} --build-arg ARTIFACT_NEXUS_URL=${ARTIFACT_NEXUS_URL}'
        ],
        scan: [
            gate: [
                high: [buildStatus: 'FAILURE', maxAllowed: 4],
                medium: [buildStatus: 'UNSTABLE', maxAllowed: 50],
                low: [buildStatus: 'UNSTABLE', maxAllowed: 100]
            ]
        ],
        push: [
            kanikoArgs: '--cache --build-arg HTTP_PROXY=${http_proxy} --build-arg IMAGE_TARGET_VERSION=${VERSION} --build-arg ARTIFACT_NEXUS_URL=${ARTIFACT_NEXUS_URL}'
        ],
        deploy_cert: [
                environment: 'CERT'
        ],
//        deploy_pre: [
//                environment: 'PRE',
//                approve: 'n391041'
//        ],
//        deploy_pro: [
//                environment: 'PRO',
//                approve: 'n391041'
//        ],
//          deploy_hf : [
//                stageName: 'Deploy shadow',
//                environment: 'SHADOW',
//                approve: 'n391041'
//        ],
//        deploy_rc : [
//                environment: 'QA',
//                approve: 'n391041'
//        ]
)
