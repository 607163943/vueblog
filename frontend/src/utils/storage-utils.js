export default {
  setLocalStorage (key, value) {
    window.localStorage.setItem(key, value)
  },
  getLocalStorage (key) {
    return window.localStorage.getItem(key)
  },
  removeLocalStorage (key) {
    window.localStorage.removeItem(key)
  },
  setSessionStorage (key, value) {
    window.sessionStorage.setItem(key, value)
  },
  getSessionStorage (key) {
    return window.sessionStorage.getItem(key)
  },
  removeSessionStorage (key) {
    window.sessionStorage.removeItem(key)
  }
}
