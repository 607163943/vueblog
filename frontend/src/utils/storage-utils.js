export default {
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
