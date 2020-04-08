# View 旋转动画

```
private void createAnim(View view, boolean isStop) {
    if (view == null) {
        return;
    } else {
        if (mAnimator != null && isStop) {
            mAnimator.pause();
        } else if (mAnimator != null && !isStop) {
            mAnimator.resume();
        } else {
            mAnimator = ObjectAnimator.ofFloat(view, View.ROTATION.getName(), 0, 360);
            view.setRotation(0);
            mAnimator.setDuration(10000);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setRepeatCount(-1);
            mAnimator.start();
        }
    }
    return;
}
```