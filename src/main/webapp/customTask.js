f= 
function () {
	console.debug("customTask");

	return function (tracker) {
		tracker.set("dimension1", tracker.get("clientId") + "_" + Date.now());
		
		// MMG plugin
		if ("undefined" === typeof tracker.get("MMGStreaming")) {
			var f = tracker.get("sendHitTask"),
			h = function () {
				function d(c) {
					var a = !1;
					try {
						//window.alert("d.c=" + c);
						document.createElement("img").src = e(!0) + "?" + c,
						a = !0
					} catch (k) {}

					//window.alert("d.a=" + a);
					return a
				}
				function e(c) {
					var a = "http://localhost:8080/collect";
					c || (a += "?tid=" + encodeURIComponent(tracker.get("trackingId")));

					//window.alert("e.a=" + a);
					return a
				}
				return {
					send: function (c) {
						var a;
						if (!(a = 2036 >= c.length && d(c))) {
							a = !1;
							try {
								a = navigator.sendBeacon && navigator.sendBeacon(e(),
										c)
							} catch (g) {}
						}
						if (!a) {
							a = !1;
							var b;
							try {
								window.XMLHttpRequest && "withCredentials" in(b = new XMLHttpRequest) && (b.open("POST", e(), !0), b.setRequestHeader("Content-Type", "text/plain"), b.send(c), a = !0)
							} catch (g) {}
						}
						return a || d(c)
					}
				}
			}
			();
			tracker.set("sendHitTask", function (d) {
				f(d);
				h.send(d.get("hitPayload"));
				tracker.set("MMGStreaming", !0)
			})
		}
		
		// OWOXBI plugin
		if ("undefined" === typeof tracker.get("OWOXBIStreaming")) {
			var f = tracker.get("sendHitTask"),
			h = function () {
				function d(c) {
					var a = !1;
					try {
						document.createElement("img").src = e(!0) + "?" + c,
						a = !0
					} catch (k) {}
					return a
				}
				function e(c) {
					var a = "https://google-analytics.bi.owox.com/collect";
					c || (a += "?tid=" + encodeURIComponent(tracker.get("trackingId")));
					return a
				}
				return {
					send: function (c) {
						var a;
						if (!(a = 2036 >= c.length && d(c))) {
							a = !1;
							try {
								a = navigator.sendBeacon && navigator.sendBeacon(e(),
										c)
							} catch (g) {}
						}
						if (!a) {
							a = !1;
							var b;
							try {
								window.XMLHttpRequest && "withCredentials" in(b = new XMLHttpRequest) && (b.open("POST", e(), !0), b.setRequestHeader("Content-Type", "text/plain"), b.send(c), a = !0)
							} catch (g) {}
						}
						return a || d(c)
					}
				}
			}
			();
			tracker.set("sendHitTask", function (d) {
				f(d);
				h.send(d.get("hitPayload"));
				tracker.set("OWOXBIStreaming", !0)
			})
		}
		
		
		
		
	}
}