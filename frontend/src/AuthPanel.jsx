import { useState } from 'react'
import { login, signup } from './api'


// 화면 위쪽의 로그인 영역
function AuthPanel({user, onLoggedIn, onLogout}) {
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [submitting, setSubmitting] = useState(false)


    //회원가입은 성공하면 바로 로그인
    async function submit(isSignup) {
        setSubmitting(true)
        setError('')
        try {
            if(isSignup) {
                await signup(username, password)
            }
            
            await login(username, password)
            setPassword('')
            await onLoggedIn()
        } catch(e) {
            console.error(e)
            setError(e.message
            )
        } finally {
            setSubmitting(false)
        }
    }

    if (user) {
        return (
            <div className="auth-panel">
                Signed in as<strong>{user.username}</strong>{' '}
                <button onClick={onLogout}>Log out</button>
            </div>
        )
    }

    return (
        <form
            className="auth-panel"
            onSubmit={(event) => {
                event.preventDefault()
                submit(false)
            }}
        >
            <input value={username} onChange={(e) =>
                setUsername(e.target.value)} placeholder="Username "/>
            <input type="password" value={password} onChange={(e) =>
                setPassword(e.target.value)} placeholder="Password" />
            
            <button type="submit" disabled={submitting}>Log in</button>
            <button type="button" disabled={submitting} onClick={() => submit(true)}>
                Sign up
            </button>
            {error && <p className="error">{error}</p>}
        </form>
    )
}

export default AuthPanel

