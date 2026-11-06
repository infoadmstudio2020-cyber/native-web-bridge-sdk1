package com.roni.library.bridge.js;

/**
 * JavaScript bootstrap and client bridge injection script generator.
 */
public final class JavaScriptBridgeScript {

    private JavaScriptBridgeScript() {
    }

    public static String getInjectionScript(String interfaceName) {
        String name = (interfaceName != null && !interfaceName.trim().isEmpty()) ? interfaceName.trim() : "NativeBridge";
        return "(function() {\n" +
                "  if (window['" + name + "Client']) return;\n" +
                "  var callbacks = {};\n" +
                "  var eventListeners = {};\n" +
                "  var reqCounter = 0;\n" +
                "\n" +
                "  var client = {\n" +
                "    call: function(action, payload) {\n" +
                "      return new Promise(function(resolve, reject) {\n" +
                "        var reqId = 'req_' + Date.now() + '_' + (++reqCounter);\n" +
                "        callbacks[reqId] = { resolve: resolve, reject: reject };\n" +
                "        var msg = JSON.stringify({\n" +
                "          id: reqId,\n" +
                "          type: 'REQUEST',\n" +
                "          action: action,\n" +
                "          payload: payload || {},\n" +
                "          timestamp: Date.now()\n" +
                "        });\n" +
                "        if (window['" + name + "'] && window['" + name + "'].postMessage) {\n" +
                "          window['" + name + "'].postMessage(msg);\n" +
                "        } else {\n" +
                "          delete callbacks[reqId];\n" +
                "          reject(new Error('Native interface " + name + " is not bound'));\n" +
                "        }\n" +
                "      });\n" +
                "    },\n" +
                "    ping: function() {\n" +
                "      if (window['" + name + "'] && window['" + name + "'].ping) {\n" +
                "        return window['" + name + "'].ping();\n" +
                "      }\n" +
                "      return 'pong_fallback';\n" +
                "    },\n" +
                "    on: function(topic, listener) {\n" +
                "      if (!eventListeners[topic]) eventListeners[topic] = [];\n" +
                "      eventListeners[topic].push(listener);\n" +
                "    },\n" +
                "    off: function(topic, listener) {\n" +
                "      if (!eventListeners[topic]) return;\n" +
                "      eventListeners[topic] = eventListeners[topic].filter(function(l) { return l !== listener; });\n" +
                "    },\n" +
                "    _handleNativeMessage: function(rawJson) {\n" +
                "      try {\n" +
                "        var msg = typeof rawJson === 'string' ? JSON.parse(rawJson) : rawJson;\n" +
                "        if (msg.type === 'RESPONSE' || msg.type === 'ERROR') {\n" +
                "          var cb = callbacks[msg.id];\n" +
                "          if (cb) {\n" +
                "            delete callbacks[msg.id];\n" +
                "            if (msg.success) { cb.resolve(msg.payload); }\n" +
                "            else { cb.reject(new Error(msg.error || 'Unknown native error')); }\n" +
                "          }\n" +
                "        } else if (msg.type === 'EVENT') {\n" +
                "          var listeners = eventListeners[msg.action] || [];\n" +
                "          listeners.forEach(function(l) { try { l(msg.payload); } catch(e){} });\n" +
                "        }\n" +
                "      } catch (err) {\n" +
                "        console.error('Failed to parse message from native:', err);\n" +
                "      }\n" +
                "    }\n" +
                "  };\n" +
                "  window['" + name + "Client'] = client;\n" +
                "})();";
    }
}
