/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.compute.options;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

/**
 * Contains options supported in the {@code ComputeService#runNodesWithTag} operation. <h2>
 * Usage</h2> The recommended way to instantiate a TemplateOptions object is to statically import
 * TemplateOptions.* and invoke a static creation method followed by an instance mutator (if
 * needed):
 * <p/>
 * <code>
 * import static org.jclouds.compute.options.TemplateOptions.Builder.*;
 * <p/>
 * ComputeService client = // get connection
 * templateBuilder.options(inboundPorts(22, 80, 8080, 443));
 * Set<? extends NodeMetadata> set = client.runNodesWithTag(tag, 2, templateBuilder.build());
 * <code>
 * 
 * @author Adrian Cole
 */
public class TemplateOptions {

   public static final TemplateOptions NONE = new TemplateOptions();

   protected int[] inboundPorts = new int[] { 22 };

   protected byte[] script;

   protected String privateKey;

   protected String publicKey;

   protected int port = -1;

   protected int seconds = -1;

   protected boolean includeMetadata;

   public int getPort() {
      return port;
   }

   public int getSeconds() {
      return seconds;
   }

   public int[] getInboundPorts() {
      return inboundPorts;
   }

   public byte[] getRunScript() {
      return script;
   }

   public String getPrivateKey() {
      return privateKey;
   }

   public String getPublicKey() {
      return publicKey;
   }

   public boolean isIncludeMetadata() {
      return includeMetadata;
   }

   public <T extends TemplateOptions> T as(Class<T> clazz) {
      return clazz.cast(this);
   }

   /**
    * When the node is started, wait until the following port is active
    */
   public TemplateOptions blockOnPort(int port, int seconds) {
      checkArgument(port > 0 && port < 65536, "port must be a positive integer < 65535");
      checkArgument(seconds > 0, "seconds must be a positive integer");
      this.port = port;
      this.seconds = seconds;
      return this;
   }

   /**
    * This script will be executed as the root user upon system startup. This script gets a
    * prologue, so no #!/bin/bash required, path set up, etc
    */
   public TemplateOptions runScript(byte[] script) {
      checkArgument(checkNotNull(script, "script").length <= 16 * 1024,
               "script cannot be larger than 16kb");
      this.script = script;
      return this;
   }

   /**
    * replaces the rsa ssh key used at login.
    */
   public TemplateOptions installPrivateKey(String privateKey) {
      checkArgument(checkNotNull(privateKey, "privateKey").startsWith(
               "-----BEGIN RSA PRIVATE KEY-----"),
               "key should start with -----BEGIN RSA PRIVATE KEY-----");
      this.privateKey = privateKey;
      return this;
   }

   /**
    * authorized an rsa ssh key.
    */
   public TemplateOptions authorizePublicKey(String publicKey) {
      checkArgument(checkNotNull(publicKey, "publicKey").startsWith("ssh-rsa"),
               "key should start with ssh-rsa");
      this.publicKey = publicKey;
      return this;
   }

   /**
    * Opens the set of ports to public access.
    */
   public TemplateOptions inboundPorts(int... ports) {
      for (int port : ports)
         checkArgument(port > 0 && port < 65536, "port must be a positive integer < 65535");
      this.inboundPorts = ports;
      return this;
   }

   public TemplateOptions withMetadata() {
      this.includeMetadata = true;
      return this;
   }

   public static class Builder {

      /**
       * @see TemplateOptions#inboundPorts
       */
      public static TemplateOptions inboundPorts(int... ports) {
         TemplateOptions options = new TemplateOptions();
         return options.inboundPorts(ports);
      }

      /**
       * @see TemplateOptions#port
       */
      public static TemplateOptions blockOnPort(int port, int seconds) {
         TemplateOptions options = new TemplateOptions();
         return options.blockOnPort(port, seconds);
      }

      /**
       * @see TemplateOptions#runScript
       */
      public static TemplateOptions runScript(byte[] script) {
         TemplateOptions options = new TemplateOptions();
         return options.runScript(script);
      }

      /**
       * @see TemplateOptions#installPrivateKey
       */
      public static TemplateOptions installPrivateKey(String rsaKey) {
         TemplateOptions options = new TemplateOptions();
         return options.installPrivateKey(rsaKey);
      }

      /**
       * @see TemplateOptions#authorizePublicKey
       */
      public static TemplateOptions authorizePublicKey(String rsaKey) {
         TemplateOptions options = new TemplateOptions();
         return options.authorizePublicKey(rsaKey);
      }

      public static TemplateOptions withDetails() {
         TemplateOptions options = new TemplateOptions();
         return options.withMetadata();
      }

   }

   @Override
   public String toString() {
      return "TemplateOptions [inboundPorts=" + Arrays.toString(inboundPorts) + ", privateKey="
               + (privateKey != null) + ", publicKey=" + (publicKey != null) + ", runScript="
               + (script != null) + ", port:seconds=" + port + ":" + seconds
               + ", metadata/details: " + includeMetadata + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.hashCode(inboundPorts);
      result = prime * result + (includeMetadata ? 1231 : 1237);
      result = prime * result + port;
      result = prime * result + ((privateKey == null) ? 0 : privateKey.hashCode());
      result = prime * result + ((publicKey == null) ? 0 : publicKey.hashCode());
      result = prime * result + Arrays.hashCode(script);
      result = prime * result + seconds;
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      TemplateOptions other = (TemplateOptions) obj;
      if (!Arrays.equals(inboundPorts, other.inboundPorts))
         return false;
      if (includeMetadata != other.includeMetadata)
         return false;
      if (port != other.port)
         return false;
      if (privateKey == null) {
         if (other.privateKey != null)
            return false;
      } else if (!privateKey.equals(other.privateKey))
         return false;
      if (publicKey == null) {
         if (other.publicKey != null)
            return false;
      } else if (!publicKey.equals(other.publicKey))
         return false;
      if (!Arrays.equals(script, other.script))
         return false;
      if (seconds != other.seconds)
         return false;
      return true;
   }
}
