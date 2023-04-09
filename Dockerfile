FROM registry.global.ccc.srvb.can.paas.cloudcenter.corp/produban/javase-11-ubi8:1.0.4.RELEASE
# https://registry.global.ccc.srvb.can.paas.cloudcenter.corp/harbor/projects/76/repositories/produban%2Fjavase-11-ubi8/tags/1.0.4.RELEASE

ARG IMAGE_TARGET_VERSION
ARG ARTIFACT_NEXUS_URL

ENV com.produban.image.version=${IMAGE_TARGET_VERSION}
LABEL com.produban.image.version=${IMAGE_TARGET_VERSION} \
      com.santander.almmc.base_image=${BASE_IMAGE} \
      com.santander.almmc.base_image_version=${BASE_IMAGE_VERSION}

USER root
RUN curl -k $ARTIFACT_NEXUS_URL -o $APP_HOME/application.jar && chown -R 20000:0 $APP_HOME
USER 20000